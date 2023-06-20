function getMemberList(params) {
    return $axios({
        url: '/user/page/' + params.page + "/" + params.pageSize,
        method: 'get',
        params: {
            username: params.username,
        }
    })
}

// 删除用户
function deleteUser(ids) {
    return $axios({
        url: '/user',
        method: 'delete',
        params: {
            ids: ids
        }
    })
}

// 修改---启用禁用接口
function enableOrDisableEmployee(params) {
    return $axios({
        url: '/user/status/' + params.status,
        method: 'put',
        params: {
            ...params
        }
    })
}

// 新增---添加用户
function addEmployee(params) {
    return $axios({
        url: '/user',
        method: 'post',
        data: {...params}
    })
}

// 修改---添加员工
function editEmployee(params) {
    return $axios({
        url: '/user',
        method: 'put',
        data: {...params}
    })
}

// 修改页面反查详情接口
function queryEmployeeById(id) {
    return $axios({
        url: `/user/${id}`,
        method: 'get'
    })
}