function getQueryParam(param) {
    const url = window.location.search;
    const params = new URLSearchParams(url);
    return params.get(param);
  }
  
  // 사용 예
//   const paramValue = getQueryParam('paramName');
//   console.log(paramValue);
  