

package com.hong.inject;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Fragment 生命周期
 * Created on 2019/5/20.
 *
 * @author upc_jxzy
 */

@Documented
@Scope
@Retention(RUNTIME)
public @interface FragmentScope {
}
