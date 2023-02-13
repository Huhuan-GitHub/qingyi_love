export function dateIsToday(date) {
  let todayDate = new Date().setHours(0, 0, 0, 0); //把今天的日期时分秒设置为00：00：00，返回一个时间戳, 
  // todayDate就是今天00：00：00时刻的时间戳
  let paramsDate = new Date(date).setHours(0, 0, 0, 0); //给new Date()传入时间，并返回传入时间的时间戳
  //时间戳相同时 True 就为今天 
  return todayDate === paramsDate
}
/**
 * 判断日期是否是当年
 * @param {*} date 
 */
export function isCurrentYear(date) {
  let currentYear = new Date().getFullYear();
  let year = new Date(date).getFullYear();
  return currentYear === year;
}