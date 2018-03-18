import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
 
/**
 * Java实现计算表达式
 * 只实现有加减乘除以及括号的运算
 * 例如: 3+12+25*(20-20/4)+10
 * @author GuoBo 2009-3-16
 * @version 1.0
 */
public class CalculateExp
{
    private static HashMap sign = new HashMap();
    /* 将运算符的优先级放入到缓存处理 */
    public CalculateExp()
    {
        sign.put(")", "3");
        sign.put("*", "2");
        sign.put("/", "2");
        sign.put("+", "1");
        sign.put("-", "1");
        sign.put("(", "0");
    }
    /**
     * @param String 输入的表达式
     * @return List 解析后的字符串元素
     * 对输入的字符串进行解析
     * 转换为需要处理的数据
     * 例如:3+12+25*(20-20/4)+10
     * 转换后的结果为:
     * List 元素为 ret = {3,+,12,+,25,*,(,20,-,20,-,20,/,4,),+,10}
     */
    public List transStr(String str)
    {
        List strList = new ArrayList();
         
        /* 获取提出数据的符号串 */
        String tmp = str.replaceAll("\\d*", "");
        /* 记录当前的运算符 */
        String curLet = null;
        /* 记录tmp字符串中第一个运算符的位置 */
        int loc = 0;
        /* 符号串长度 */
        int len = tmp.length();
        for (int i = 0; i < len; i++)
        {
            curLet = tmp.substring(i, i + 1);
            loc = str.indexOf(curLet);
            /* 如果当前处理字符为( 或者 ) */
 
            if (!"".equals(str.substring(0, loc).trim()))
            {
                strList.add(str.substring(0, loc).trim());
            }
            strList.add(str.substring(loc, loc + 1));
            str = str.substring(loc + 1);
        }
        if (0 < str.length())
        {
            strList.add(str.trim());
        }
        return strList;
    }
 
    /**
     * 将表达式从中缀表达式转换为后缀表达式(波兰式)
     * @Param List 解析后的表达式的列表
     * @return String[] 转换后的表达式字符串数组
     */
    public String[] midToEnd(List midList)
    {
        Stack embl = new Stack();
        Stack result = new Stack();
         
        Iterator it = midList.iterator();
        String curStr = null;
        while (it.hasNext())
        {
            curStr = (String) it.next();
             
            /* 确认是否式字符串 */
            if(sign.containsKey(curStr))
            {
                /* 如果符号栈为空 或者符号为( */
                if (0 == embl.size() || "(".equals(curStr))
                {
                    embl.push(curStr);
                }
                else
                {
                    /*如果符号为) 符号栈需要出栈,直到匹配一个(为止 */
                    if(")".equals(curStr))
                    {
                        while(!"(".equals((String)embl.peek()))
                        {
                            if(0 >= embl.size())
                            {
                                return null;
                            }
                            result.push(embl.pop());
                        }
                        embl.pop();
                    }
                    else
                    {
                        int p1 = Integer.parseInt((String) sign.get(curStr));
                        int p2 = Integer.parseInt((String) sign.get(embl.peek()));
                         
                        /* 如果当前字符的优先级大于栈顶符号的优先级 */
                        if (p1 > p2)
                        {
                            embl.push(curStr);
                        }
                        else
                        {
                            while (p1 <= p2 || embl.size() > 0)
                            {
                                result.push(embl.pop());
                                if(0 == embl.size())
                                {
                                    break;
                                }
                                p2 = Integer.parseInt((String) sign.get(embl.peek()));
                            }
                            embl.push(curStr);
                        }
                    }
                }
            }
            else
            {
                result.push(curStr);
            }
        }
         
        while (0 < embl.size())
        {
            result.push(embl.pop());
        }
         
        int len = result.size();
        String[] ret = new String[len];
        for (int i = 0; i < len; i++)
        {
            ret[len - i - 1] = (String) result.pop();
        }
         
        return ret;
    }
     
    /**
     * 解析后缀表达式,返回对应的运算结果
     * @param String[] endStr 转换后的后缀表达式
     * @return Object 返回运算结果 如果表达式有误直接打印"Input Error"
     */
    public Object calculate(String[] endStr)
    {
        int len = endStr.length;
        Stack calc = new Stack();
        double p2;
        double p1;
        for (int i = 0; i < len; i++)
        {
            if (sign.containsKey(endStr[i]))
            {
                try
                {
                    p2 = Double.parseDouble((String) calc.pop());
                    p1 = Double.parseDouble((String) calc.pop());
                    calc.push(String.valueOf(simpleCalc(p1, p2,endStr[i])));
                }
                catch(NumberFormatException ex)
                {
                    ex.printStackTrace();
                    return "Input Error";
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                    return "Input Error";
                }
            }
            else
            {
                calc.push(endStr[i]);
            }
        }
         
        if (1 == calc.size())
        {
            return calc.pop();
        }
        else
        {
            return "Input Error";
        }
    }
     
    /**
     * 实现底层的运算函数
     * @param double p1 数字1
     * @param double p1 数字2
     * @param String oper 运算符 +-/*
     */
    public double simpleCalc(double p1, double p2, String oper)
    {
         
        switch(oper.charAt(0))
        {
          case '+':
            return p1 + p2;
          case '-':
            return p1 - p2;
          case '*':
            return p1 * p2;
          case '/':
            return p1 / p2;
          default:
            return p1;
        }
    }
     
    /**
     * 主控函数
     */
    public static void main(String[] args)
    {
        CalculateExp ce = new CalculateExp();
        String tmp = "3+12+25*(20-20/4+10";
        String ret = (String) ce.calculate(ce.midToEnd(ce
                .transStr(tmp)));
        double value = 0;
    try
    {
        value = Double.parseDouble(ret);
    }
    catch (NumberFormatException ex)
    {
        System.out.print(ret);
    }
        System.out.print(value);
    }
}