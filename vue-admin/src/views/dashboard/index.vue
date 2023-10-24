<!--
 * @Author: ToLearningOk 568541648@qq.com
 * @Date: 2023-03-03 19:16:15
 * @LastEditors: ToLearningOk 568541648@qq.com
 * @LastEditTime: 2023-03-10 12:48:45
 * @FilePath: \undefinedc:\Users\56854\Desktop\sg-vue-admin\src\views\dashboard\index.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div class="dashboard-container">
    <div class="dashboard-text">
      博客管理系统
    </div>
    <div>
      欢迎访问
      <!-- <a href="https://space.bilibili.com/">https://space.bilibili.com/</a>    -->
    </div>
    <div>
      
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name'
    ])
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
