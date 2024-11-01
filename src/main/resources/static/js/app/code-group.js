$(document).ready(function() {
    $("#codeGroupListId").click(function () {
        console.log("codeGroupList");
    })

    $("#codeGroupReadId").click(function () {
        console.log("codeGroupRead")
    })

    $("#codeGroupRegisterId").click(function () {
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
            .then(response => response.json())
            .then(data => console.log(data))
            .catch(error => console.error('Error:', error));
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