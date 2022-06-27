设计思想:对于业务无侵入增加上传文件的水印功能
整体设计:
        利用Aop切入上传文件Controller层解析到MultipartFile参数信息然后用转换好的替换Tomcat里原始文件的缓存流达到对业务无侵入
        

使用注意点:
        1.引入必须加入spring aop 否则不生效
        2.可根据业务接收到的Dto判断是否需要添加文件

扩展点:
        利用增加自定义的格式的 AddWaterMarkUtil.addNewTypes.put("xls",new PdfAddWaterMark());
        实现 AddWaterMark接口 自己扩展其他文件格式

用法:
Aop 模式: 必须加入spring-aop-starter

加入下面的yml配置:
        addWaterMark:
                enable: true
                aop: true

Controller层:
        @PostMapping("/xxx")
        @AddWaterMarkAn(whetherAdd = "#fileDto.flag",content = "#fileDto.content",mode = WaveMarkMode.PICTURE,picPath = "/Users/hejie/Desktop/12.png")
        public void hh(@RequestParam("hh")MultipartFile file,FileDto fileDto,HttpServletRequest httpServletRequest) throws IOException {
        
            file.transferTo(new File("/Users/hejie/Desktop/11/" + originalFilename));

        }


拦截器模式:
        加入下面的yml配置
                addWaterMark:
                        enable: true
                        interceptor: true
        前端传入参数:
                        flag:true
                        content:xxx
                        picPath:图片路径
                        mode:pic   //模式
Controller层:
        @PostMapping("/hh")
        public void hh(HttpServletRequest httpServletRequest) throws IOException {

                MultipartHttpServletRequest request = (MultipartHttpServletRequest) httpServletRequest;
                Map<String, MultipartFile> fileMap = request.getFileMap();
                Object[] objects = fileMap.values().toArray();
                MultipartFile multipartFile = (MultipartFile) objects[0];
                String originalFilename = multipartFile.getOriginalFilename();
                multipartFile.transferTo(new File("/Users/hejie/Desktop/11/" + originalFilename));

        }

直接使用:
        @Autowired
        AddWaterMark wordAddWaterMark;

        默认是文字模式:
                wordAddWaterMark.transfer("/Users/hejie/Desktop/12.docx","/Users/hejie/Desktop/1111.docx","22222");
        图片模式:
                wordAddWaterMark.setWaveMarkMode(WaveMarkMode.PICTURE);
                wordAddWaterMark.setPicPath("/Users/hejie/Desktop/12.png");
                wordAddWaterMark.transfer("/Users/hejie/Desktop/12.docx","/Users/hejie/Desktop/1111.docx","22222");

可能遇到问题：
        无法引入 spire.doc maven的settings配置文件里把mirrors和profiles节点都注释了,重新拉取



