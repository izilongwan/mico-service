; ((doc, win) => {
  const BASE_URL = 'http://localhost:9999/sse'
  // 用时间戳模拟登录用户
  const userId = Date.now();
  const oDom = {
    textarea: doc.querySelector('.J_textarea'),
    btn: doc.querySelector('.J_btn'),
    list: doc.querySelector('.J_list'),
    listCount: doc.querySelector('.J_listCount'),
  }
  const data = {
    list: [],
  }
  let source = null;

  const init = () => {
    if (!window.EventSource) {
      console.log("你的浏览器不支持SSE");
      return
    }

    initEventSource()
    bindEvent()
  }

  function bindEvent() {
    source.addEventListener('open', onOpen)
    source.addEventListener('message', onMessage)
    source.addEventListener('error', onError)
    // 监听窗口关闭事件，主动去关闭sse连接，如果服务端设置永不过期，浏览器关闭后手动清理服务端数据
    win.onbeforeunload = closeSse

    oDom.btn.addEventListener('click', onSendMessage, false)
  }

  function onSendMessage() {
    let { value } = oDom.textarea

    value = value.trim()

    if (!value.length) {
      return
    }

    http({ url: `${ BASE_URL}/send/batch`, method: 'POST', body: JSON.stringify(JSON.parse(value)) })

    data.list.push(value)
    addListContent(value)
    oDom.textarea.value = ''
  }

  function addListContent(value) {
    oDom.listCount.innerHTML = `LIST (${data.list.length})`
    oDom.list.innerHTML += `
      <li>${value}</li>
    `
  }

  function http(option) {
    const { url = '', method = 'GET', body = null, headers = {
      'Content-Type': 'application/json',
    } } = option

    return fetch(url, {
      body,
      method,
      headers,
    })
      .then(json => json.json())
      .then(rs => rs)
  }

  function initEventSource() {
    // 建立连接
    source = new EventSource(`${BASE_URL}/connect/${ userId }`);
    console.log("连接用户=" + userId);

    return source
  }

  function onOpen() {
    console.log("建立连接。。。");
  }

  function onMessage(e) {
    try {
      const data = JSON.parse(e.data)
      console.log(data)
    } catch (error) {
      console.error(error)
    }
  }

  function onError(e) {
    if (e.readyState === EventSource.CLOSED) {
      console.log("连接关闭");
      return
    }

    console.log(e);
  }

  // 关闭Sse连接
  function closeSse() {
    source.close();
    fetch(`${BASE_URL}/disconnect/${ userId }`)
  }

  init()
})(document, window);
