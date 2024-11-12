function getMonthNameFromJs(monthIndex) {
  const formatter = new Intl.DateTimeFormat(undefined, { month: 'long' });
  const date = new Date(2024, monthIndex);
  const monthName = formatter.format(date);
  console.log("Detected Locale Month Name:", monthName);  // 检查输出的月份名称
  return monthName;
}

// 将函数挂载到 globalThis，使 Wasm 环境可以调用它
if (typeof globalThis !== 'undefined') {
  globalThis.getMonthNameFromJs = getMonthNameFromJs;
}