/**
 * 将大数字格式化为 k w
 * 例如：12345678->1234.6w
 * @param {*} num 需要格式化的数字 
 */
export function formatNumber(num) {
  return num >= 1e3 && num < 1e4 ? (num / 1e3).toFixed(1) + 'k' : num >= 1e4 ? (num / 1e4).toFixed(1) + 'w' : num
}