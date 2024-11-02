$(document).ready(function () {
    $("#codeGroupListId").click(function () {
        console.log("codeGroupList");
    })

    $("#codeGroupReadId").click(function () {
        console.log("codeGroupRead")
    })

    $("#codeGroupRegisterId").click(function () {
        // 이전 에러 표시 초기화
        clearErrors();

        const codeGroupObj = {
            groupCode: $("#groupCodeId").val(),
            groupName: $("#groupNameId").val(),
        };

        console.log(JSON.stringify(codeGroupObj));

        fetch('/codegroups', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(codeGroupObj)
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(errorData => {
                        throw errorData;
                    })
                }
                response.json()
            })
            .then(data => console.log(data))
            .catch(error => {
                console.error('Error:', error);
                if (error.errors) {
                    showErrors(error.errors);
                } else {
                    // 일반적인 에러 처리
                    alert(error.message || '처리 중 오류가 발생했습니다.');
                }
            });
    })

    $("#codeGroupDeleteId").click(function () {
        console.log("codeGroupDelete")
    })

    $("#codeGroupModifyId").click(function () {
        console.log("codeGroupModify")
    })

    $("#codeGroupResetId").click(function () {
        console.log("codeGroupReset")
    })
});