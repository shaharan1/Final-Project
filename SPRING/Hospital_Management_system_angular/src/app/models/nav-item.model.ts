export interface NavItem {
  label: string;
  icon: string;
  route: string;
  section: string;
}

export interface NavGroup {
  label: string;
  items: NavItem[];
}
