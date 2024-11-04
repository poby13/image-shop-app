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

        // 입력값 가져오기
        const groupCode = $("#groupCodeId").val().trim();
        const groupName = $("#groupNameId").val().trim();

        // 클라이언트 측 유효성 검사
        let hasError = false;
        const errors = {};

        // 그룹 코드 검사
        if (!groupCode) {
            errors.groupCode = messages.required.groupCode;
            hasError = true;
        } else if (groupCode.length !== 3) {
            errors.groupCode = messages.size.groupCode;
            hasError = true;
        }

        // 그룹명 검사
        if (!groupName) {
            errors.groupName = messages.required.groupName;
            hasError = true;
        } else if (groupName.length >= 3 && groupName <= 30) {
            errors.groupName = messages.size.groupName;
            hasError = true;
        }

        // 유효성 검사 실패 시 에러 표시하고 요청 중단
        if (hasError) {
            showErrors(errors);
            return false;
        }

        // 유효성 검사 통과 시 객체 생성
        const codeGroupObj = {
            groupCode: groupCode,
            groupName: groupName,
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
                    alert(error.message || messages.process.fail);
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

    // 실시간 유효성 검사
    $("#groupCodeId, #groupNameId").on('input', function() {
        const field = $(this).attr('id').replace('Id', '');
        const value = $(this).val().trim();
        const errorElement = $(`#${field}Error`);

        // 입력값이 변경될 때마다 해당 필드의 유효성 검사
        if (!value) {
            $(this).addClass('is-invalid');
            errorElement.text(`${field === 'groupCode' ? messages.required.groupCode : messages.required.groupName}`).show();
        } else if (value.length !== 3) {
            $(this).addClass('is-invalid');
            errorElement.text(`${field === 'groupCode' ? messages.size.groupCode : messages.size.groupName}`).show();
        } else {
            $(this).removeClass('is-invalid');
            errorElement.hide().text('');
        }
    });
});