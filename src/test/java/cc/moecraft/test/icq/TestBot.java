package cc.moecraft.test.icq;

import cc.moecraft.icq.PicqBotX;
import cc.moecraft.icq.exceptions.HttpServerStartFailedException;
import cc.moecraft.icq.exceptions.VersionIncorrectException;

/**
 * 此类由 Hykilpikonna 在 2018/05/24 创建!
 * Created by Hykilpikonna on 2018/05/24!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
public class TestBot
{
    public static void main(String[] args)
    {
        // 创建机器人对象 ( 信息发送URL, 发送端口, 接收端口, 是否DEBUG )
        PicqBotX bot = new PicqBotX("127.0.0.1", 31091, 31092, false);

        try
        {
            bot.getEventManager()
                    .registerListener(new TestListener()) // 注册监听器
                    .registerListener(new RequestListener()); // 可以注册多个监听器
            if (!bot.isDebug()) bot.getEventManager().registerListener(new SimpleTextLoggingListener()); // 这个只是在不开Debug的时候用来Log消息的

            // 启用指令管理器, 启用的时候会自动注册指令
            // 这些字符串是指令前缀, 比如!help的前缀就是!
            bot.enableCommandManager("bot -", "!", "/", "~");

            bot.startBot(); // 启动机器人
        }
        catch (HttpServerStartFailedException | VersionIncorrectException | IllegalAccessException | InstantiationException e)
        {
            e.printStackTrace(); // 启动失败, 结束程序
        }
    }
}
