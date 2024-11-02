const formControlEl = $('.form-control');
const errorMessageEl = $('.error-message');

// 에러 표시 함수
function showErrors(errors) {
    Object.keys(errors).forEach(field => {
        const inputElement = $(`#${field}Id`);
        const errorElement = $(`#${field}Error`);

        inputElement.addClass('is-invalid');
        errorElement.text(errors[field]).show();

        // 첫 번째 에러 필드에 포커스
        if (field === Object.keys(errors)[0]) {
            inputElement.focus();
        }
    });
}

// 에러 표시 초기화 함수
function clearErrors() {
    formControlEl.removeClass('is-invalid');
    errorMessageEl.hide().text('');
}

// 입력 필드 변경 시 해당 필드의 에러 표시 제거
formControlEl.on('input', function () {
    $(this).removeClass('is-invalid');
    $(`#${this.id.replace('Id', '')}Error`).hide().text('');
});