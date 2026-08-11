import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TestMasterModel } from '../../../../models/testMasterModel';
import { TestMasterService } from '../../../../services/test-master.service';
import { LabConfigService } from '../../../../services/lab-config.service';
import { InterpretationRule, ReferenceRange, TestMasterDetail, TestParameter } from '../../../../models/lab-config.model';

@Component({
  selector: 'app-reference-range-management',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reference-range-management.component.html',
  styleUrl: './reference-range-management.component.css',
})
export class ReferenceRangeManagementComponent implements OnInit {

  masters: TestMasterModel[] = [];
  filteredMasters: TestMasterModel[] = [];
  searchKeyword = '';
  loading = true;

  selectedMaster: TestMasterModel | null = null;
  detail: TestMasterDetail | null = null;
  detailLoading = false;

  expandedParam = new Set<number>();
  editingRange: { rowId: number; saving: boolean } | null = null;

  newRange: any = {
    genderScope: 'ANY',
    minAgeYears: null,
    maxAgeYears: null,
    minValue: null,
    maxValue: null,
    criticalLow: null,
    criticalHigh: null,
    displayRange: '',
    priority: 0,
    active: true,
  };

  newRule: any = {
    parameterStatus: 'ABNORMAL',
    valueMatch: '',
    interpretationText: '',
    displayOrder: 0,
    active: true,
  };

  constructor(
    private testMasterService: TestMasterService,
    private labConfigService: LabConfigService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.testMasterService.getAll().subscribe({
      next: (list) => {
        this.masters = list.filter(m => m.active !== false);
        this.filteredMasters = this.masters;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.loading = false;
        alert('Failed to load test masters.');
      }
    });
  }

  filterMasters(): void {
    const kw = this.searchKeyword.toLowerCase();
    this.filteredMasters = this.masters.filter(m =>
      m.testName.toLowerCase().includes(kw) || m.testCode.toLowerCase().includes(kw)
    );
  }

  selectMaster(master: TestMasterModel): void {
    this.selectedMaster = master;
    this.detail = null;
    this.detailLoading = true;
    this.expandedParam.clear();
    this.labConfigService.getTestMasterDetail(master.id!).subscribe({
      next: (d) => {
        this.detail = d;
        this.detailLoading = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.detailLoading = false;
        alert('Failed to load test parameters.');
      }
    });
  }

  toggleParam(id: number): void {
    if (this.expandedParam.has(id)) this.expandedParam.delete(id);
    else this.expandedParam.add(id);
  }

  isExpanded(id: number): boolean {
    return this.expandedParam.has(id);
  }

  parameterRanges(p: TestParameter): ReferenceRange[] {
    return p.referenceRanges || [];
  }

  parameterRules(p: TestParameter): InterpretationRule[] {
    return p.interpretationRules || [];
  }

  rangeLabel(r: ReferenceRange): string {
    const parts: string[] = [];
    if (r.genderScope && r.genderScope !== 'ANY') parts.push(r.genderScope);
    if (r.minAgeYears != null || r.maxAgeYears != null) {
      parts.push(`${r.minAgeYears ?? 0}-${r.maxAgeYears ?? '∞'} yrs`);
    }
    return parts.length ? parts.join(' · ') : 'Default';
  }

  editRange(r: ReferenceRange): void {
    r.editing = true;
    this.cdr.markForCheck();
  }

  cancelRangeEdit(r: ReferenceRange): void {
    r.editing = false;
    this.cdr.markForCheck();
  }

  saveRange(r: ReferenceRange): void {
    this.editingRange = { rowId: r.id!, saving: true };
    this.labConfigService.updateRange(r.id!, {
      id: r.id,
      testParameterId: r.testParameterId,
      genderScope: r.genderScope,
      minAgeYears: r.minAgeYears,
      maxAgeYears: r.maxAgeYears,
      minValue: r.minValue,
      maxValue: r.maxValue,
      criticalLow: r.criticalLow,
      criticalHigh: r.criticalHigh,
      displayRange: r.displayRange,
      priority: r.priority,
      active: r.active,
    }).subscribe({
      next: () => {
        r.editing = false;
        this.editingRange = null;
        this.cdr.markForCheck();
      },
      error: () => {
        this.editingRange = null;
        alert('Failed to save reference range.');
      }
    });
  }

  deleteRange(r: ReferenceRange): void {
    if (!confirm('Delete this reference range?')) return;
    this.labConfigService.deleteRange(r.id!).subscribe({
      next: () => {
        const param = this.detail?.parameters.find(p => p.id === r.testParameterId);
        if (param?.referenceRanges) {
          param.referenceRanges = param.referenceRanges.filter(x => x.id !== r.id);
        }
        this.cdr.markForCheck();
      },
      error: () => alert('Failed to delete reference range.')
    });
  }

  addRange(param: TestParameter): void {
    if (!this.newRange.displayRange.trim() && this.newRange.minValue == null && this.newRange.maxValue == null) {
      alert('Enter a reference range value.');
      return;
    }
    this.labConfigService.createRange({
      testParameterId: param.id!,
      genderScope: this.newRange.genderScope,
      minAgeYears: this.newRange.minAgeYears,
      maxAgeYears: this.newRange.maxAgeYears,
      minValue: this.newRange.minValue,
      maxValue: this.newRange.maxValue,
      criticalLow: this.newRange.criticalLow,
      criticalHigh: this.newRange.criticalHigh,
      displayRange: this.newRange.displayRange,
      priority: this.newRange.priority,
      active: this.newRange.active,
    }).subscribe({
      next: (created) => {
        if (!param.referenceRanges) param.referenceRanges = [];
        param.referenceRanges.push(created);
        this.newRange = {
          genderScope: 'ANY', minAgeYears: null, maxAgeYears: null,
          minValue: null, maxValue: null, criticalLow: null, criticalHigh: null,
          displayRange: '', priority: 0, active: true,
        };
        this.cdr.markForCheck();
      },
      error: () => alert('Failed to create reference range.')
    });
  }

  addRule(param: TestParameter): void {
    if (!this.newRule.interpretationText.trim()) {
      alert('Enter interpretation text.');
      return;
    }
    this.labConfigService.createRule({
      testParameterId: param.id!,
      parameterStatus: this.newRule.parameterStatus,
      valueMatch: this.newRule.valueMatch,
      interpretationText: this.newRule.interpretationText,
      displayOrder: this.newRule.displayOrder,
      active: this.newRule.active,
    }).subscribe({
      next: (created) => {
        if (!param.interpretationRules) param.interpretationRules = [];
        param.interpretationRules.push(created);
        this.newRule = { parameterStatus: 'ABNORMAL', valueMatch: '', interpretationText: '', displayOrder: 0, active: true };
        this.cdr.markForCheck();
      },
      error: () => alert('Failed to create interpretation rule.')
    });
  }

  deleteRule(rule: InterpretationRule, param: TestParameter): void {
    if (!confirm('Delete this interpretation rule?')) return;
    this.labConfigService.deleteRule(rule.id!).subscribe({
      next: () => {
        if (param.interpretationRules) {
          param.interpretationRules = param.interpretationRules.filter(x => x.id !== rule.id);
        }
        this.cdr.markForCheck();
      },
      error: () => alert('Failed to delete interpretation rule.')
    });
  }

  saveRule(rule: InterpretationRule): void {
    this.labConfigService.updateRule(rule.id!, {
      id: rule.id,
      testParameterId: rule.testParameterId,
      parameterStatus: rule.parameterStatus,
      valueMatch: rule.valueMatch,
      interpretationText: rule.interpretationText,
      displayOrder: rule.displayOrder,
      active: rule.active,
    }).subscribe({
      next: () => {
        rule.editing = false;
        this.cdr.markForCheck();
      },
      error: () => alert('Failed to update interpretation rule.')
    });
  }

  statusOptions = ['NORMAL', 'LOW', 'HIGH', 'CRITICAL_LOW', 'CRITICAL_HIGH', 'POSITIVE', 'NEGATIVE', 'REACTIVE', 'NON_REACTIVE', 'BORDERLINE', 'ABNORMAL'];
  genderOptions = ['ANY', 'MALE', 'FEMALE'];

  getInitials(name: string): string {
    if (!name) return '?';
    return name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2);
  }
}
