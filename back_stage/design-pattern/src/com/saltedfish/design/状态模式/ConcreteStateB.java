package com.saltedfish.design.状态模式;
/**
 * 
 * @ClassName: ConcreteStateA 
 * @Description: 具体状态类B
 * @author: lkd
 * @date: 2017年4月17日 上午11:38:51
 */
public class ConcreteStateB implements State{
	
    @Override
    public void handle(String sampleParameter) {
        
        System.out.println("ConcreteStateB handle ：" + sampleParameter);
    }

}
