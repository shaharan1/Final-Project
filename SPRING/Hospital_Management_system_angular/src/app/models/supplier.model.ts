export interface SupplierModel {
  id?: number;
  name: string;
  contactPerson: string;
  phone: string;
  email: string;
  address: string;
  companyName: string;
  tradeLicense: string;
  drugLicense: string;
  website: string;
  notes: string;
  totalDue?: number;
  active?: boolean;
  createdDate?: string;
}
