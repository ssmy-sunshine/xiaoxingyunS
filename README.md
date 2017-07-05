## API文档 : 
```
var mescroll = new MeScroll("mescroll", options); //创建mescroll对象
```  

<table border="1" cellspacing="0">
	<tr align="center"><td colspan="6">参数配置 options</td></tr>
	<tr align="center"><td></td> <td colspan="2">下拉刷新  down</td> <td colspan="2">上拉加载 up</td></tr>
	<tr align="center">
		<td>参数名</td>
		
		<td>默认值</td>
		<td>说明</td>
		
		<td>默认值</td>
		<td>说明</td>
	</tr>
	<tr align="center">
		<td>use</td>
		
		<td>true</td>
		<td>是否启用下拉刷新</td>
		
		<td>true</td>
		<td>是否启用上拉加载</td>
	</tr>
	<tr align="center">
		<td>auto</td>
		
		<td>true</td>
		<td>是否在初始化完毕之后自动执行下拉刷新的回调</td>
		
		<td>false</td>
		<td>是否在初始化完毕之后自动执行上拉加载的回调</td>
	</tr>
	<tr align="center">
		<td>autoshowLoading</td>
		
		<td>false</td>
		<td>如果设置auto=true(在初始化完毕之后自动执行下拉刷新的回调),那么是否显示下拉刷新的进度</td>
	</tr>
	<tr align="center">
		<td>isLock</td>
		
		<td>false</td>
		<td>是否锁定下拉刷新</td>
		
		<td>false</td>
		<td>是否锁定上拉加载</td>
	</tr>
	<tr align="center">
		<td>isBoth</td>
		
		<td>false</td>
		<td>下拉刷新时,如果滑动到列表底部是否可以同时触发上拉加载</td>
		
		<td>false</td>
		<td>上拉加载时,如果滑动到列表顶部是否可以同时触发下拉刷新</td>
	</tr>
	<tr align="center">
		<td>offset</td>
		
		<td>80</td>
		<td>在列表顶部,下拉大于80px,松手即可触发下拉刷新的回调</td>
		
		<td>100</td>
		<td>列表滚动到距离底部小于100px,即可触发上拉加载的回调</td>
	</tr>
	<tr align="center">
		<td>outOffsetRate</td>
		
		<td>0.2</td>
		<td>在列表顶部,下拉的距离大于offset时,改变下拉区域高度比例;值越接近0,高度变化越小,表现为越往下越难拉</td>
	</tr>
	<tr align="center">
		<td>mustToTop</td>
		
		<td>iOS默认值为false,其他为true</td>
		<td>是否必须滑动到顶部才能下拉;因为列表回弹效果(-webkit-overflow-scrolling:touch)是iOS专属样式,所以iOS默认false,其他为true</td>
	</tr>
	<tr align="center">
		<td>hardwareClass</td>
		
		<td>"mescroll-hardware"</td>
		<td>硬件加速样式,解决iOS下拉因隐藏进度条而闪屏的问题,参见mescroll.css</td>
		
		<td>"mescroll-hardware"</td>
		<td>硬件加速样式,使动画更流畅,参见mescroll.css</td>
	</tr>
	<tr align="center">
		<td>warpClass</td>
		
		<td>"mescroll-downwarp"</td>
		<td>下拉刷新的布局容器样式,参见mescroll.css</td>
		
		<td>"mescroll-upwarp"</td>
		<td>上拉加载的布局容器样式,参见mescroll.css</td>
	</tr>
	<tr align="center">
		<td>resetClass</td>
		
		<td>"mescroll-downwarp-reset"</td>
		<td>下拉刷新高度重置的动画,参见mescroll.css</td>
	</tr>
	<tr align="center">
		<td>htmlContent</td>
		
		<td>'&lt;p class="downwarp-progress"&gt;&lt;/p&gt;&lt;p class="downwarp-tip"&gt;下拉刷新&lt;/p&gt;'</td>
		<td>下拉刷新的布局内容</td>
	</tr>
</table>

# mescroll
## mescroll -- 精致的下拉刷新和上拉加载js框架 (JS framework for pull-refresh and pull-up-loading)

1. 原生js,不依赖jquery,zepto等框架

2. 完美运行于android,iOS,手机浏览器

3. 兼容PC主流浏览器

4. 参数自由搭配,可定制,可拓展

5. 超详细注释,源码通俗易懂

6. 案例丰富,经典

## 目录:  

* <a href="#功能亮点-">功能亮点 </a> <br/>
* <a href="#快速入门-">快速入门 </a> <br/>
* <a href="#api文档-">API文档 </a> <br/>
* <a href="#基础案例-base-demos-">基础案例 base demos </a> <br/>
* <a href="#高级案例-senior-demos-">高级案例 senior demos </a> <br/>

## 功能亮点 :

1. 自动判断和提示列表无任何数据或无更多数据

2. 无需手动判断处理列表的页码,时间等变量

3. 可配置回到顶部按钮

4. 可指定列表滚动到任何位置,包括滚动到底部

5. 可配置列表数据不满屏时,自动加载下一页

6. 可临时锁定下拉刷新和上拉加载  

7. 一个界面可支持多个下拉刷新,上拉加载


## 快速入门 :

1. 引用 **mescroll.min.css** , **mescroll.min.js**

2. 拷贝以下布局结构:  
```
        <div id="mescroll" class="mescroll">
            //列表内容,如:<ul>列表数据</ul> ...
        </div>  
```  

3. 创建MeScroll对象:  
```
        var mescroll = new MeScroll("mescroll", {
    		down: {
			callback: downCallback //下拉刷新的回调
		},
		up: {
			callback: upCallback //上拉加载回调,简写callback:function(page){upCallback(page);}
		}
	});
```  

4. 处理回调:
```
        //下拉刷新的回调
        function downCallback(){
            $.ajax({
                url: 'xxxxxx',
                success: function(data){
                	//联网成功的回调,隐藏下拉刷新的状态;
        		mescroll.endSuccess();//无参
        		//设置数据
        		//setXxxx(data);//自行实现 TODO
               },
               error: function(data){
               		//联网失败的回调,隐藏下拉刷新的状态
        	        mescroll.endErr();
                }
            });
        }

        //上拉加载的回调 page = {num:1, size:10}; num:当前页 从1开始, size:每页数据条数
        function upCallback(page){
            $.ajax({
                url: 'xxxxxx?num='+ page.num +"&size="+ page.size,
                success: function(data){
			//联网成功的回调,隐藏下拉刷新和上拉加载的状态;
			//参数data.length:当前页的数据总数
			//mescroll会根据data.length自动判断列表如果无任何数据,则提示空;
			//列表如果无下一页数据,则提示无更多数据;
			//如果不传data.length,则仅隐藏下拉刷新和上拉加载的状态.例如downCallback
                	mescroll.endSuccess(data.length);
		        //设置列表数据
		        //setListData(data);//自行实现 TODO
                },
                error: function(data){
                	//联网失败的回调,隐藏下拉刷新和上拉加载的状态
	                mescroll.endErr();
                }
         });
        }
```  

## API文档 :  

<table border="1" cellspacing="10">
	<tr><td>Data</td><td>Data</td></tr>
	<tr><td>Data</td><td>Data</td></tr>
</table>  

## 基础案例 base demos :  

## 高级案例 senior demos :

#### 1. [【淘宝 v6.8.0】APP的下拉刷新上拉加载](https://github.com/mescroll/mescroll/tree/master/demo/taobao)
![](https://github.com/mescroll/mescroll/raw/master/demo/taobao/taobao.gif) 
<br/><br/><br/>
#### 2. [【京东 v6.1.0】APP的下拉刷新上拉加载](https://github.com/mescroll/mescroll/tree/master/demo/jingdong)
![](https://github.com/mescroll/mescroll/raw/master/demo/jingdong/jingdong.gif) 
<br/><br/><br/>
#### 3. [【美团 v8.2.3】APP的下拉刷新上拉加载](https://github.com/mescroll/mescroll/tree/master/demo/meituan)
![](https://github.com/mescroll/mescroll/raw/master/demo/meituan/meituan.gif) 
<br/><br/><br/>
#### 4. [【新浪微博 v7.6.1】APP的下拉刷新上拉加载](https://github.com/mescroll/mescroll/tree/master/demo/xinlang)
![](https://github.com/mescroll/mescroll/raw/master/demo/xinlang/xinlang.gif) 
<br/><br/><br/>
#### 5. [【贝贝 v6.0.0】APP的下拉刷新上拉加载](https://github.com/mescroll/mescroll/tree/master/demo/beibei)
![](https://github.com/mescroll/mescroll/raw/master/demo/beibei/beibei.gif) 
<br/><br/><br/>
#### 6. [【雅布力 v2.4.0】APP的下拉刷新上拉加载](https://github.com/mescroll/mescroll/tree/master/demo/yabuli)
![](https://github.com/mescroll/mescroll/raw/master/demo/yabuli/yabuli.gif) 
<br/><br/><br/>
#### 7. [【美囤妈妈 v2.0.5】APP的下拉刷新上拉加载](https://github.com/mescroll/mescroll/tree/master/demo/meitunmama)
![](https://github.com/mescroll/mescroll/raw/master/demo/meitunmama/meitunmama.gif) 
<br/><br/><br/>
#### 8. [【知乎 v3.53.0】APP的下拉刷新上拉加载](https://github.com/mescroll/mescroll/tree/master/demo/zhihu)
![](https://github.com/mescroll/mescroll/raw/master/demo/zhihu/zhihu.gif) 

