function loginApi(data) {
  return $axios({
    'url': '/user/login?isAdmin=1',
    'method': 'post',
    data
  })
}

function logoutApi(){
  return $axios({
    'url': '/user/logout',
    'method': 'get',
  })
}
