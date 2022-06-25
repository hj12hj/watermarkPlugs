设计思想:对于业务无侵入增加上传文件的水印功能
整体设计:
        利用Aop切入上传文件Controller层解析到MultipartFile参数信息然后用转换好的替换Tomcat里原始文件的缓存流达到对业务无侵入
        

使用注意点:
        1.引入必须加入spring aop 否则不生效
        2.可根据业务接收到的Dto判断是否需要添加文件

扩展点:
        利用增加自定义的格式的 AddWaterMarkUtil.addWaterMarkMap.put("111",new PdfAddWaterMark());
        实现 AddWaterMark接口