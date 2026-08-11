export type LabResultType = 'NUMERIC' | 'POSITIVE_NEGATIVE' | 'TEXT' | 'MULTI_OPTION';

export interface ReferenceRange {
  id?: number;
  testParameterId?: number;
  genderScope: string;
  minAgeYears?: number;
  maxAgeYears?: number;
  minValue?: number;
  maxValue?: number;
  criticalLow?: number;
  criticalHigh?: number;
  displayRange?: string;
  priority?: number;
  active?: boolean;
  editing?: boolean;
}

export interface InterpretationRule {
  id?: number;
  testParameterId?: number;
  parameterStatus: string;
  valueMatch?: string;
  interpretationText: string;
  displayOrder?: number;
  active?: boolean;
  editing?: boolean;
}

export interface TestParameter {
  id?: number;
  testMasterId: number;
  parameterName: string;
  parameterCode: string;
  unit?: string;
  resultType: LabResultType;
  allowedValues?: string;
  displayOrder?: number;
  decimalPrecision?: number;
  normalText?: string;
  active?: boolean;
  referenceRanges: ReferenceRange[];
  interpretationRules: InterpretationRule[];
}

export interface TestMasterDetail {
  id: number;
  testCode: string;
  testName: string;
  standardPrice: number;
  normalRange?: string;
  active?: boolean;
  parameters: TestParameter[];
}

export interface LabRule {
  id?: number;
  ruleCode: string;
  ruleName: string;
  conditions: string;
  finalImpression: string;
  recommendation?: string;
  priority?: number;
  active?: boolean;
}

export interface InterpretPreview {
  parameterId: number;
  resultValue?: string;
  unit?: string;
  status: string;
  statusLabel: string;
  interpretation?: string;
  referenceRangeDisplay?: string;
  abnormal?: boolean;
  critical?: boolean;
}
