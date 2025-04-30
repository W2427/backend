'use strict';

(() = > {

  http.sendForm(
  document.querySelector('form'),
  {port: 8810},
  result = > {

  if(result.success
)
{

  localStorage.setItem('userId', result.data.id);

  for (let kvp of location.search.substr(1).split('&')) {
    let [key, value] = kvp.split('=');
    key = decodeURIComponent(key);
    if (key === 'target') {
      location.href = decodeURIComponent(value);
      return;
    }
  }

}

}
)
;

})
();
