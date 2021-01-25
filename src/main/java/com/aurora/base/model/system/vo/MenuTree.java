package com.aurora.base.model.system.vo;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单资源树
 * @author :PHQ
 * @date：2020/5/29
 **/
@Accessors(chain = true)
@EqualsAndHashCode(callSuper=false)
@Builder
@Data
@AllArgsConstructor //全参构造函数
@NoArgsConstructor  //无参构造函数
public class MenuTree implements Serializable {

      private String  title;
      private String   key;
      private int      idKey;
      private boolean  disabled = false;

      private List<MenuTree> children;

}
