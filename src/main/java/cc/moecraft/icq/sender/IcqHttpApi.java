package cc.moecraft.icq.sender;

import cc.moecraft.icq.event.EventManager;
import cc.moecraft.icq.event.events.local.EventLocalSendDiscussMessage;
import cc.moecraft.icq.event.events.local.EventLocalSendGroupMessage;
import cc.moecraft.icq.event.events.local.EventLocalSendPrivateMessage;
import cc.moecraft.icq.sender.returndata.RawReturnData;
import cc.moecraft.icq.sender.returndata.ReturnData;
import cc.moecraft.icq.sender.returndata.ReturnListData;
import cc.moecraft.icq.sender.returndata.returnpojo.ReturnPojoBase;
import cc.moecraft.icq.sender.returndata.returnpojo.get.*;
import cc.moecraft.icq.sender.returndata.returnpojo.send.RMessageReturnData;
import cc.moecraft.icq.utils.MapBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.xiaoleilu.hutool.http.HttpUtil;
import com.xiaoleilu.hutool.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 此类由 Hykilpikonna 在 2018/19/24 创建!
 * Created by Hykilpikonna on 2018/19/24!
 * Github: https://github.com/hykilpikonna
 * QQ: admin@moecraft.cc -OR- 871674895
 *
 * @author Hykilpikonna
 */
public class IcqHttpApi
{
    // 发送区
    public static final String SEND_PRIVATE_MSG = "send_private_msg";
    public static final String SEND_GROUP_MSG = "send_group_msg";
    public static final String SEND_DISCUSS_MSG = "send_discuss_msg";
    public static final String SEND_LIKE = "send_like";

    // 撤回消息单独一类哈哈哈哈
    public static final String DELETE_MSG = "delete_msg";

    // 应用设置区
    public static final String SET_GROUP_KICK = "set_group_kick";
    public static final String SET_GROUP_BAN = "set_group_ban";
    public static final String SET_GROUP_ANONYMOUS_BAN = "set_group_anonymous_ban";
    public static final String SET_GROUP_WHOLE_BAN = "set_group_whole_ban";
    public static final String SET_GROUP_ADMIN = "set_group_admin";
    public static final String SET_GROUP_ANONYMOUS = "set_group_anonymous";
    public static final String SET_GROUP_CARD = "set_group_card";
    public static final String SET_GROUP_LEAVE = "set_group_leave";
    public static final String SET_GROUP_SPECIAL_TITLE = "set_group_special_title";
    public static final String SET_DISCUSS_LEAVE = "set_discuss_leave";
    public static final String SET_FRIEND_ADD_REQUEST = "set_friend_add_request";
    public static final String SET_GROUP_ADD_REQUEST = "set_group_add_request";

    // ICQ(酷Q, 以及HTTP插件)设置区
    public static final String SET_RESTART = "set_restart";
    public static final String SET_RESTART_PLUGIN = "set_restart_plugin";
    public static final String CLEAN_DATA_DIR = "clean_data_dir";

    // 应用内获取区
    public static final String GET_LOGIN_INFO = "get_login_info";
    public static final String GET_STRANGER_INFO = "get_stranger_info";
    public static final String GET_GROUP_LIST = "get_group_list";
    public static final String GET_GROUP_MEMBER_INFO = "get_group_member_info";
    public static final String GET_GROUP_MEMBER_LIST = "get_group_member_list";
    public static final String GET_FRIEND_LIST = "_get_friend_list";

    // ICQ(酷Q, 以及HTTP插件)获取区
    public static final String GET_VERSION_INFO = "get_version_info";
    public static final String GET_STATUS = "get_status";

    @Deprecated
    public static final String SEND_MSG = "send_msg";  // 这个不需要, 因为最后也要指定类型

    private final String baseURL;
    private final EventManager eventManager;

    public IcqHttpApi(EventManager eventManager, String baseUrl, int port)
    {
        this.eventManager = eventManager;
        baseUrl = baseUrl.toLowerCase();
        if (!baseUrl.contains("http://")) baseUrl = "http://" + baseUrl;

        baseURL = baseUrl + ":" + port + "/";
    }

    /**
     * 发送请求
     * @param request 请求
     * @param parameters 参数
     * @return 响应
     */
    public JsonElement send(String request, Map<String, Object> parameters)
    {
        return new JsonParser().parse(HttpUtil.post(baseURL + request, new JSONObject(parameters).toString(), 5000));
    }

    /**
     * 发送请求 封装x1
     * @param request 请求
     * @param parameters 参数
     * @return 响应
     */
    public JsonElement send(String request, Object... parameters)
    {
        return send(request, MapBuilder.build(String.class, Object.class, parameters));
    }

    /**
     * 发送请求 封装
     * @param request 请求
     * @param parameters 参数
     * @return 响应
     */
    public RawReturnData sendReturnRaw(String request, Map<String, Object> parameters)
    {
        return new Gson().fromJson(send(request, parameters), RawReturnData.class);
    }

    /**
     * 发送请求 封装
     * @param request 请求
     * @param parameters 参数
     * @return 响应
     */
    public RawReturnData sendReturnRaw(String request, Object... parameters)
    {
        return sendReturnRaw(request, MapBuilder.build(String.class, Object.class, parameters));
    }

    /**
     * 发送请求 封装
     * @param typeOfT 返回数据类型
     * @param request 请求
     * @param parameters 参数
     * @return 响应
     */
    public <T extends ReturnPojoBase> ReturnData<T> send(Type typeOfT, String request, Map<String, Object> parameters)
    {
        return sendReturnRaw(request, parameters).processData(typeOfT);
    }

    /**
     * 发送请求 封装
     * @param typeOfT 返回数据类型
     * @param request 请求
     * @param parameters 参数
     * @return 响应
     */
    public <T extends ReturnPojoBase> ReturnData<T> send(Class<T> typeOfT, String request, Object... parameters)
    {
        return send(typeOfT, request, MapBuilder.build(String.class, Object.class, parameters));
    }

    /**
     * 发送请求 封装
     * @param typeOfT 返回数据类型
     * @param request 请求
     * @param parameters 参数
     * @return 响应
     */
    public <T extends ReturnPojoBase> ReturnListData<T> sendReturnList(Type typeOfT, String request, Map<String, Object> parameters)
    {
        return sendReturnRaw(request, parameters).processDataAsList(typeOfT);
    }

    /**
     * 发送请求 封装
     * @param typeOfT 返回数据类型
     * @param request 请求
     * @param parameters 参数
     * @return 响应
     */
    public <T extends ReturnPojoBase> ReturnListData<T> sendReturnList(Class<T> typeOfT, String request, Object... parameters)
    {
        return sendReturnList(typeOfT, request, MapBuilder.build(String.class, Object.class, parameters));
    }

    /**
     * 发送私聊消息
     * @param qq      QQ号
     * @param message 消息
     */
    public ReturnData<RMessageReturnData> sendPrivateMsg(long qq, String message)
    {
        EventLocalSendPrivateMessage event = new EventLocalSendPrivateMessage(qq, message, false);
        eventManager.call(event);
        return send(RMessageReturnData.class, SEND_PRIVATE_MSG, "user_id", event.getId(), "message", event.getMessage(), "auto_escape", event.isAutoEscape());
    }

    /**
     * 发送私聊消息
     * @param qq      QQ号
     * @param message 消息
     * @param autoEscape 是否纯文本发送
     */
    public ReturnData<RMessageReturnData> sendPrivateMsg(long qq, String message, boolean autoEscape)
    {
        EventLocalSendPrivateMessage event = new EventLocalSendPrivateMessage(qq, message, autoEscape);
        eventManager.call(event);
        return send(RMessageReturnData.class, SEND_PRIVATE_MSG, "user_id", event.getId(), "message", event.getMessage(), "auto_escape", event.isAutoEscape());
    }

    /**
     * 发送群聊消息
     * @param groupId 群ID
     * @param message 消息
     */
    public ReturnData<RMessageReturnData> sendGroupMsg(long groupId, String message)
    {
        EventLocalSendGroupMessage event = new EventLocalSendGroupMessage(groupId, message, false);
        eventManager.call(event);
        return send(RMessageReturnData.class, SEND_GROUP_MSG, "group_id", event.getId(), "message", event.getMessage(), "auto_escape", event.isAutoEscape());
    }

    /**
     * 发送群聊消息
     * @param groupId 群ID
     * @param message 消息
     * @param autoEscape 是否纯文本发送
     */
    public ReturnData<RMessageReturnData> sendGroupMsg(long groupId, String message, boolean autoEscape)
    {
        EventLocalSendGroupMessage event = new EventLocalSendGroupMessage(groupId, message, autoEscape);
        eventManager.call(event);
        return send(RMessageReturnData.class, SEND_GROUP_MSG, "group_id", event.getId(), "message", event.getMessage(), "auto_escape", event.isAutoEscape());
    }

    /**
     * 发送讨论组消息
     * @param groupId 讨论组ID
     * @param message 消息
     */
    public ReturnData<RMessageReturnData> sendDiscussMsg(long groupId, String message)
    {
        EventLocalSendDiscussMessage event = new EventLocalSendDiscussMessage(groupId, message, false);
        eventManager.call(event);
        return send(RMessageReturnData.class, SEND_DISCUSS_MSG, "discuss_id", event.getId(), "message", event.getMessage(), "auto_escape", event.isAutoEscape());
    }

    /**
     * 发送讨论组消息
     * @param groupId 讨论组ID
     * @param message 消息
     * @param autoEscape 是否纯文本发送
     */
    public ReturnData<RMessageReturnData> sendDiscussMsg(long groupId, String message, boolean autoEscape)
    {
        EventLocalSendDiscussMessage event = new EventLocalSendDiscussMessage(groupId, message, autoEscape);
        eventManager.call(event);
        return send(RMessageReturnData.class, SEND_DISCUSS_MSG, "discuss_id", event.getId(), "message", event.getMessage(), "auto_escape", event.isAutoEscape());
    }

    /**
     * 撤回消息
     * @param messageId 消息ID
     */
    public RawReturnData deleteMsg(long messageId)
    {
        return sendReturnRaw(DELETE_MSG, "message_id", messageId);
    }

    /**
     * 发送好友赞
     * @param qq    QQ号
     * @param times 赞的次数，每个好友每天最多 10 次
     */
    public RawReturnData sendLike(long qq, long times)
    {
        return sendReturnRaw(SEND_LIKE, "user_id", qq, "times", times);
    }

    /**
     * 群组踢人
     * @param groupId 群号
     * @param qq      QQ
     */
    public RawReturnData setGroupKick(long groupId, long qq)
    {
        return sendReturnRaw(SET_GROUP_KICK, "user_id", qq, "group_id", groupId);
    }

    /**
     * 群组单人禁言
     * @param groupId  群号
     * @param qq       QQ
     * @param duration 禁言时长，单位秒，0 表示取消禁言
     */

    public RawReturnData setGroupBan(long groupId, long qq, long duration)
    {
        return sendReturnRaw(SET_GROUP_BAN, "user_id", qq, "group_id", groupId, "duration", duration);
    }

    /**
     * 群组匿名用户禁言
     * @param flag     要禁言的匿名用户的 flag（需从群消息上报的数据中获得）
     * @param groupId  群号
     * @param duration 禁言时长，单位秒，无法取消匿名用户禁言
     */
    public RawReturnData setGroupAnonymousBan(String flag, long groupId, long duration)
    {
        return sendReturnRaw(SET_GROUP_ANONYMOUS_BAN, "flag", flag, "group_id", groupId, "duration", duration);
    }

    /**
     * 群组全员禁言
     * @param groupId 群号
     * @param enable  是否禁言
     */
    public RawReturnData setGroupWholeBan(long groupId, boolean enable)
    {
        return sendReturnRaw(SET_GROUP_WHOLE_BAN, "group_id", groupId, "enable", enable);
    }

    /**
     * 群组设置管理员
     * @param groupId 群号
     * @param qq      要设置管理员的 QQ 号
     * @param enable  true 为设置，false 为取消
     */
    public RawReturnData setGroupAdmin(long groupId, long qq, boolean enable)
    {
        return sendReturnRaw(SET_GROUP_ADMIN, "group_id", groupId, "user_id", qq, "enable", enable);
    }

    /**
     * 群组设置匿名
     * @param groupId 群号
     * @param enable  是否允许匿名聊天
     */
    public RawReturnData setGroupAnonymous(long groupId, boolean enable)
    {
        return sendReturnRaw(SET_GROUP_ANONYMOUS, "group_id", groupId, "enable", enable);
    }

    /**
     * 设置群名片（群备注）
     * @param groupId 群号
     * @param qq      要设置的 QQ 号
     * @param card  群名片内容，不填或空字符串表示删除群名片
     */
    public RawReturnData setGroupCard(long groupId, long qq, String card)
    {
        return sendReturnRaw(SET_GROUP_CARD, "group_id", groupId, "user_id", qq, "card", card);
    }

    /**
     * 退出群组
     * @param groupId 群号
     * @param dismiss 是否解散，如果登录号是群主，则仅在此项为 true 时能够解散
     */
    public RawReturnData setGroupLeave(long groupId, boolean dismiss)
    {
        return sendReturnRaw(SET_GROUP_LEAVE, "group_id", groupId, "is_dismiss", dismiss);
    }

    /**
     * 设置群组专属头衔
     * @param groupId 群号
     * @param qq 要设置的QQ号
     * @param specialTitle 专属头衔，不填或空字符串表示删除专属头衔
     */
    public RawReturnData setGroupSpecialTitle(long groupId, long qq, String specialTitle)
    {
        return sendReturnRaw(SET_GROUP_SPECIAL_TITLE, "group_id", groupId, "user_id", qq, "special_title", specialTitle);
    }

    /**
     * 设置群组专属头衔
     * @param groupId 群号
     * @param qq 要设置的QQ号
     * @param specialTitle 专属头衔，不填或空字符串表示删除专属头衔
     * @param duration 专属头衔有效期，单位秒，-1 表示永久，不过此项似乎没有效果，可能是只有某些特殊的时间长度有效，有待测试
     */
    public RawReturnData setGroupSpecialTitle(long groupId, long qq, String specialTitle, long duration)
    {
        return sendReturnRaw(SET_GROUP_SPECIAL_TITLE, "group_id", groupId, "user_id", qq, "special_title", specialTitle, "duration", duration);
    }

    /**
     * 退出讨论组
     * @param discussId 讨论组 ID（正常情况下看不到，需要从讨论组消息上报的数据中获得）
     */
    public RawReturnData setDiscussLeave(long discussId)
    {
        return sendReturnRaw(SET_DISCUSS_LEAVE, "discuss_id", discussId);
    }

    /**
     * 处理加好友请求
     * @param flag 加好友请求的 flag（需从上报的数据中获得）
     * @param approve 是否同意请求
     */
    public RawReturnData setFriendAndRequest(String flag, boolean approve)
    {
        return sendReturnRaw(SET_FRIEND_ADD_REQUEST, "flag", flag, "approve", approve);
    }

    /**
     * 处理加好友请求
     * @param flag 加好友请求的 flag（需从上报的数据中获得）
     * @param approve 是否同意请求
     * @param remark 添加后的好友备注（仅在同意时有效）
     */
    public RawReturnData setFriendAndRequest(String flag, boolean approve, String remark)
    {
        return sendReturnRaw(SET_FRIEND_ADD_REQUEST, "flag", flag, "approve", approve, "remark", remark);
    }

    /**
     * 处理加群请求／邀请
     * @param flag 加好友请求的 flag（需从上报的数据中获得）
     * @param type add 或 invite，请求类型（需要和上报消息中的 sub_type 字段相符）
     * @param approve 是否同意请求／邀请
     * @param reason 拒绝理由（仅在拒绝时有效）
     */
    public RawReturnData setGroupAndRequest(String flag, String type, boolean approve, String reason)
    {
        return sendReturnRaw(SET_GROUP_ADD_REQUEST, "flag", flag, "type", type, "approve", approve, "reason", reason);
    }

    /**
     * 同意加群请求／邀请
     * @param flag 加好友请求的 flag（需从上报的数据中获得）
     * @param type add 或 invite，请求类型（需要和上报消息中的 sub_type 字段相符）
     */
    public RawReturnData approveGroupRequest(String flag, String type)
    {
        return setGroupAndRequest(flag, type, true, "");
    }

    /**
     * 拒绝加群请求／邀请
     * @param flag 加好友请求的 flag（需从上报的数据中获得）
     * @param type add 或 invite，请求类型（需要和上报消息中的 sub_type 字段相符）
     * @param reason 拒绝理由
     */
    public RawReturnData rejectGroupRequest(String flag, String type, String reason)
    {
        return setGroupAndRequest(flag, type, false, reason);
    }

    /**
     * 重启酷 Q，并以当前登录号自动登录（需勾选快速登录）
     */
    public RawReturnData setRestart()
    {
        return sendReturnRaw(SET_RESTART);
    }

    /**
     * 重启酷 Q，并以当前登录号自动登录（需勾选快速登录）
     * @param cleanCache 是否清除酷Q当前登录号缓存数据
     */
    public RawReturnData setRestart(boolean cleanCache)
    {
        return sendReturnRaw(SET_RESTART, "clean_cache", cleanCache);
    }

    /**
     * 重启 HTTP API 插件
     */
    public RawReturnData setRestartPlugin()
    {
        return sendReturnRaw(SET_RESTART_PLUGIN);
    }

    /**
     * 获取登录号信息
     */
    public ReturnData<RLoginInfo> getLoginInfo()
    {
        return send(RLoginInfo.class, GET_LOGIN_INFO);
    }

    /**
     * 获取陌生人信息
     * @param qq QQ号
     * @param noCache 是否不使用缓存（使用缓存可能更新不及时，但响应更快）
     */
    public ReturnData<RStrangerInfo> getStrangerInfo(long qq, boolean noCache)
    {
        return send(RStrangerInfo.class, GET_STRANGER_INFO, "user_id", qq, "no_cache", noCache);
    }

    /**
     * 获取陌生人信息, 默认使用缓存
     * @param qq QQ号
     */
    public ReturnData<RStrangerInfo> getStrangerInfo(long qq)
    {
        return getStrangerInfo(qq, true);
    }

    /**
     * 获取群列表
     */
    public ReturnListData<RGroup> getGroupList()
    {
        return sendReturnList(RGroup.class, GET_GROUP_LIST);
    }

    /**
     * 获取群成员信息
     * @param groupId 群号
     * @param qq QQ 号（不可以是登录号）
     */
    public ReturnData<RGroupMemberInfo> getGroupMemberInfo(long groupId, long qq)
    {
        return send(RGroupMemberInfo.class, GET_GROUP_MEMBER_INFO, "group_id", groupId, "user_id", qq);
    }

    /**
     * 获取群成员列表
     * @param groupId 群号
     */
    public ReturnListData<RGroupMemberInfo> getGroupMemberList(long groupId)
    {
        return sendReturnList(RGroupMemberInfo.class, GET_GROUP_MEMBER_LIST, "group_id", groupId);
    }

    /**
     * (实验性) 获取好友列表
     */
    public ReturnListData<RFriendList> getFriendList()
    {
        return sendReturnList(RFriendList.class, GET_FRIEND_LIST);
    }

    /**
     * 获取酷 Q 及 HTTP API 插件的版本信息
     */
    public ReturnData<RVersionInfo> getVersionInfo()
    {
        return send(RVersionInfo.class, GET_VERSION_INFO);
    }

    /**
     * 获取插件运行状态
     */
    public ReturnData<RStatus> getStatus()
    {
        return send(RStatus.class, GET_STATUS);
    }

    /**
     * 获取插件运行状态
     */
    public RawReturnData cleanDataDir()
    {
        return sendReturnRaw(CLEAN_DATA_DIR);
    }
}
