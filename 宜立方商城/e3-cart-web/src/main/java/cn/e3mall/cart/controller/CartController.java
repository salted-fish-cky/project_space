package cn.e3mall.cart.controller;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车处理Controller
 */
@Controller
public class CartController {

    @Autowired
    private ItemService itemService;

    @Value("${COOKIE_CART_EXPIRE}")
    private Integer COOKIE_CART_EXPIRE;

    @Autowired
    private CartService cartService;


    @RequestMapping("/cart/add/{itemId}")
    public String addCart(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num,
                          HttpServletRequest request, HttpServletResponse response){

        //判断用户是否为登录状态
        TbUser user = (TbUser) request.getAttribute("user");
        //如果是登录状态，把购物车写入redis
        if(user != null){
            //保存到服务端
            cartService.addCart(user.getId(),itemId,num);
            return "cartSuccess";
        }

        boolean flag = false;
        //从cookie中取购物车列表
        List<TbItem> cartList = getCartListFromCookie(request);
        //判断商品在商品列表中是否存在
        for(TbItem item : cartList){

            if(itemId.longValue() == item.getId()){
                //如果存在数量相加
                item.setNum(item.getNum()+num);
                flag  = true;
                break;
            }

        }
        //如果不存在，根据商品id查询商品信息，得到一个TbITem对象
        if(!flag){
            TbItem tbItem = itemService.getItemById(itemId);
            tbItem.setNum(num);
            if(StringUtils.isNotBlank(tbItem.getImage())){
                tbItem.setImage(tbItem.getImage().split(",")[0]);
            }
            //把商品添加到商品列表
            cartList.add(tbItem);

        }
        //写入cookie
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(cartList),
                COOKIE_CART_EXPIRE,true);
        //返回添加成功页面
        return "cartSuccess";
    }

    /**
     * 从cookie中取购物车列表的处理
     * @param request
     * @return
     */
    private List<TbItem> getCartListFromCookie(HttpServletRequest request){
        String json = CookieUtils.getCookieValue(request, "cart", true);
        //判断json是否为空
        if(StringUtils.isBlank(json)){
            return new ArrayList<>();
        }
        //把json转换成商品列表
        List<TbItem> tbItems = JsonUtils.jsonToList(json, TbItem.class);
        return tbItems;
    }

    /**
     * 展示购物车列表
     * @param request
     * @return
     */
    @RequestMapping("/cart/cart")
    public String showCartList(HttpServletRequest request,HttpServletResponse response){
        //从cookie中取出购物车列表
        List<TbItem> cartList = getCartListFromCookie(request);

        //判断用户是否为登录状态
        TbUser user = (TbUser) request.getAttribute("user");
        //如果是登录状态
        if(user != null){
            //从cookie中取购物车列表
            //如果不为空，把cookie中的购物车商品和服务端的购物车商品合并
            cartService.mergeCart(user.getId(),cartList);
            //把cookie中的购物车删除
            CookieUtils.deleteCookie(request,response,"cart");
            //从服务端取购物车列表
            cartList = cartService.getCartList(user.getId());
        }

        //未登录状态
        request.setAttribute("cartList",cartList);
        return "cart";
    }


    /**
     * 更新购物车商品数量
     */
    @RequestMapping(value = "/cart/update/num/{itemId}/{num}",method = RequestMethod.POST)
    @ResponseBody
    public E3Result updateCartNum(@PathVariable Long itemId,@PathVariable Integer num,
                                  HttpServletRequest request,HttpServletResponse response){

        //判断用户是否为登录状态
        TbUser user = (TbUser) request.getAttribute("user");
        if(user != null){
            cartService.updateCartNum(user.getId(),itemId,num);
            return E3Result.ok();
        }
        //从cookie中取购物车列表
        List<TbItem> cartList = getCartListFromCookie(request);
        //遍历商品列表找到对应的商品
        for (TbItem item : cartList){
            if(item.getId().longValue() == itemId){
                //更新数量
                item.setNum(num);
                break;
            }


        }
        //把购物车列表写回cookie
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(cartList),
                COOKIE_CART_EXPIRE,true);
        //返回成功
        return E3Result.ok();
    }


    /**
     * 删除购物车商品
     */
    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCartItem(@PathVariable Long itemId,
                                 HttpServletRequest request,HttpServletResponse response){
        //判断用户是否为登录状态
        TbUser user = (TbUser) request.getAttribute("user");
        if(user != null){
            cartService.deleteCartItem(user.getId(),itemId);
            return "redirect:/cart/cart.html";
        }
        //从cookie中取购物车列表
        List<TbItem> cartList = getCartListFromCookie(request);
        //遍历列表，找到要删除的商品
        for(TbItem item : cartList){
            if(item.getId().longValue() == itemId){
                //删除商品
                cartList.remove(item);
                break;
            }

        }

        //把购物车列表写入cookie
        CookieUtils.setCookie(request,response,"cart",JsonUtils.objectToJson(cartList),
                COOKIE_CART_EXPIRE,true);
        //返回逻辑视图
        return "redirect:/cart/cart.html";
    }



}
