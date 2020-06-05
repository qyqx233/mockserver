function postData(url, data) {
    // Default options are marked with *
    return fetch(url, {
      body: JSON.stringify(data), // must match 'Content-Type' header
      cache: 'no-cache', // *default, no-cache, reload, force-cache, only-if-cached
      credentials: 'same-origin', // include, same-origin, *omit
      headers: {
        'content-type': 'application/json'
      },
      method: 'POST', // *GET, POST, PUT, DELETE, etc.
      mode: 'cors', // no-cors, cors, *same-origin
      redirect: 'follow', // manual, *follow, error
      referrer: 'no-referrer', // *client, no-referrer
    })
      .then(response => response.json()) // parses response to JSON
  }

  function fetchSetCategory(that) {
    postData('/api/service/list', {})
    .then((resp) => { 
      let saved = localStorage.getItem('category')
      if(resp.code === 0) {
        that.categorys = resp.data
        if(that.categorys.filter((e) => e === saved).length !== 0) {
          that.category = saved
        }
        return
      }
      that.category = saved
     })
  }