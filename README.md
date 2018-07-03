# CalendarPriceList

> 使用RecyvlerView打造价格日历界面

### 使用说明：

#### 日历跳转

##### 单选日期：

    CalendarActivity.startForResult(activity,
        CalendarActivity.MODE.SINGLE.toNumber(), null, 0x1024);
      
##### 往返日期：

    CalendarActivity.startForResult(activity,
        CalendarActivity.MODE.ROUND.toNumber(), null, 0x1024);

#### 获得结果

    @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == 0x1024) {
             if (data == null || resultCode != RESULT_OK) {
                 return;
             }
             // 出发日期
             depDateBean = (DateBean) data.getSerializableExtra("resultDate");
             // 回程日期
             backDateBean = (DateBean) data.getSerializableExtra("resultDate_back");
         }
     }

效果图：

![效果图](https://github.com/wuguojin/CalendarPriceList/blob/master/device-2018-07-03-160102.png?raw=true)

![效果图](https://github.com/wuguojin/CalendarPriceList/blob/master/device-2018-07-03-160123.png?raw=true)