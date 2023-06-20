function isExternal (path) {
  return /^(https?:|mailto:|tel:)/.test(path);
}

function isCellPhone (val) {
  return /^1(3|4|5|6|7|8)\d{9}$/.test(val);
}

//校验账号
function checkUserName (rule, value, callback){
  if (value == "") {
    callback(new Error("请输入用户名"))
  } else if (!(/^[0-9a-zA-Z_]+$/.test(value))) {
    callback(new Error("请输入正确的用户名(【数字】或【字母】或【下划线】组成)!"))
  } else if (value.length < 6 || value.length > 15) {
    callback(new Error("长度在 [6, 15] 之间"))
  } else {
    callback()
  }
}

// 校验密码
function checkPassword (rule, value, callback){
  if (value == "" || value === undefined) {
    callback(new Error("请输入密码"))
  } else if (!(/^[0-9a-zA-Z_]+$/.test(value))) {
    callback(new Error("请输入正确的密码(【数字】或【字母】或【下划线】组成)!"))
  } else if (value.length < 6 || value.length > 15) {
    callback(new Error("长度在 [6, 15] 之间"))
  } else {
    callback()
  }
  // 开发测试阶段, 不需要这么麻烦
}

//校验邮箱
function checkEmail (rule, value, callback){
  if (value == "") {
    callback(new Error("请输入邮箱地址"))
  } else if (!checkEmailReg(value)) {
    callback(new Error("请输入正确的邮箱地址"))
  } else {
    callback()
  }
}

function checkEmailReg(value) {
  return /^\w+@[a-zA-Z0-9]{2,10}(?:\.[a-z]{2,4}){1,3}$/.test(value);
}

function checkPhone (rule, value, callback){
  // let phoneReg = /(^1[3|4|5|6|7|8|9]\d{9}$)|(^09\d{8}$)/;
  if (value == "") {
    callback(new Error("请输入手机号"))
  } else if (!isCellPhone(value)) {//引入methods中封装的检查手机格式的方法
    callback(new Error("请输入正确的手机号"))
  } else {
    callback()
  }
}