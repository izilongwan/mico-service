; (() => {
  const BASE_URL = 'http://localhost:9999/sse'
  // 用时间戳模拟登录用户
  const userId = Date.now();
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
    window.onbeforeunload = closeSse
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
})();
