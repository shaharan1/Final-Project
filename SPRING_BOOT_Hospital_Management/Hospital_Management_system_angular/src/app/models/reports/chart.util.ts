export function toLabelValue(
  data: any,
  labelKey: string,
  valueKey: string
): { labels: string[]; values: number[] } {
  const labels: string[] = [];
  const values: number[] = [];
  if (!data) return { labels, values };

  if (Array.isArray(data)) {
    for (const d of data) {
      labels.push(String(d?.[labelKey] ?? ''));
      values.push(Number(d?.[valueKey] ?? 0));
    }
  } else {
    for (const [k, v] of Object.entries(data as Record<string, any>)) {
      labels.push(String(k));
      values.push(Number(v ?? 0));
    }
  }
  return { labels, values };
}
