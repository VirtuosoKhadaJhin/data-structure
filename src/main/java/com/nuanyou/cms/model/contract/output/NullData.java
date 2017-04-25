package com.nuanyou.cms.model.contract.output;


import io.swagger.annotations.ApiModel;

import java.util.Objects;


@ApiModel(description = "")
public class NullData {
  


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NullData nullData = (NullData) o;
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash();
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class NullData {\n");
    
    sb.append("}\n");
    return sb.toString();
  }
}
