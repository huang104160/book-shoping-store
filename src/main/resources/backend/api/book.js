// 查询列表接口
const getBookPage = (params) => {
    return $axios({
        url: '/book/page/' + params.page + "/" + params.pageSize,
        method: 'get',
        params: {
            bookName: params.bookName,
            //涉及到运算，需要将它放在最后一个参数，如果random在bookName前面，bookName将无法加入到url中
            random: Math.random(),
        }
    })
}

// 删除接口
const deleteBook = (ids) => {
    return $axios({
        url: '/book',
        method: 'delete',
        params: {ids}
    })
}

// 修改接口
const editBook = (params) => {
    return $axios({
        url: '/book',
        method: 'put',
        data: {...params}
    })
}

// 新增接口
const addBook = (params) => {
    return $axios({
        url: '/book',
        method: 'post',
        data: {...params}
    })
}

// 查询详情
const queryBookById = (id) => {
    return $axios({
        url: `/book/${id}`,
        method: 'get'
    })
}

// 获取菜品分类列表
const getCategoryList = (params) => {
    return $axios({
        url: '/category/list',
        method: 'get',
        params
    })
}

// 文件down预览
const commonDownload = (params) => {
    return $axios({
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
        },
        url: '/common/download',
        method: 'get',
        params
    })
}

// 起售停售---批量起售停售接口
const bookStatusByStatus = (params) => {
    return $axios({
        url: `/book/status/${params.status}`,
        method: 'PUT',
        params: {ids: params.id}
    })
}

// 提交表单保存图书信息
const submitForm = (params, action) => {
    return $axios({
        url: "/book?action=" + action,
        method: "POST",
        data: {
            ...params,
        }
    })
}