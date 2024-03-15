package com.cooba;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class TestEntity {
    private Long id;
    private long aLong;
    private int anInt;
    private Long longType;
    private Integer integer;
    private String str;
    private BigDecimal bigDecimal;
    private LocalDateTime localDateTime;
    private Date date;
}
