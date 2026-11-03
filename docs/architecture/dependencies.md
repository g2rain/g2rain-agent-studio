# 依赖边界

固定方向：`startup -> biz -> api`。API 不依赖 Biz/Startup；Biz 实现 API，不反向依赖 Startup；Controller -> Service -> DAO，Converter 不发起数据库或网络调用。

其他服务可使用 Studio 的查询和明确的能力包契约；宽泛的跨服务写入不是默认协作方式。管理端写入经 Gateway 到 Studio 本地写用例。
