function getMonthNameFromJs(monthIndex) {
  const formatter = new Intl.DateTimeFormat(undefined, { month: 'long' });
  const date = new Date(2024, monthIndex);
  const monthName = formatter.format(date);
  console.log("Detected Locale Month Name:", monthName);
  return monthName;
}

if (typeof globalThis !== 'undefined') {
  globalThis.getMonthNameFromJs = getMonthNameFromJs;
}