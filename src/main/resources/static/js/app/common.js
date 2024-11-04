// 상수값 지정
const PAGE_SIZE = 10; // 페이지네이션 페이지당 아이템 수

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

// URL 파라미터 가져오는 함수
function getUrlParameter(name, defaultValue) {
      const urlParams = new URLSearchParams(window.location.search);
    const value = urlParams.get(name);

    // 값이 없거나 숫자가 아닌 경우 기본값 반환
    if (!value || isNaN(value)) {
        return defaultValue;
    }

    // 숫자로 변환하여 반환
    return parseInt(value);
}
