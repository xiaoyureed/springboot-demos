package io.github.xiaoyu.dockercomposedemo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author xiaoyu
 * @since 1.0
 */
@Data
@Entity
public class User {
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false)
    private String ip;
    @Column(nullable = false)
    private int times;
}
