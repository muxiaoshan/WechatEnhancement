package me.firesun.wechat.enhancement.plugin;

import android.content.ContentValues;
import android.text.TextUtils;

import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.firesun.wechat.enhancement.util.HookParams;

import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.newInstance;

public class AutoAgreeFriend implements IPlugin {
    @Override
    public void hook(final XC_LoadPackage.LoadPackageParam lpparam) {
        findAndHookMethod(HookParams.getInstance().SQLiteDatabaseClassName, lpparam.classLoader,
                HookParams.getInstance().SQLiteDatabaseInsertMethod,String.class,String.class, ContentValues.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                /*XposedBridge.log("开始劫持了~");
                XposedBridge.log("参数1 = " + param.args[0]);
                XposedBridge.log("参数2 = " + param.args[1]);
                XposedBridge.log("参数3 = " + param.args[2]);*/
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                ContentValues contentValues = (ContentValues) param.args[2];
                String tableName = (String) param.args[0];
                /*XposedBridge.log("劫持结束了~");
                XposedBridge.log("参数1 tableName = " + param.args[0]);
                XposedBridge.log("参数2 = " + param.args[1]);*/
                //判断新好友
                if (!TextUtils.isEmpty(tableName) && tableName.equals("fmessage_msginfo")) {
                    XposedBridge.log(String.format("收到好友请求%s", contentValues.toString()));
                    Integer isSend = contentValues.getAsInteger("isSend");
                    Integer type = contentValues.getAsInteger("type");
                    final String talker = contentValues.getAsString("talker");
                    final String content = contentValues.getAsString("content");
                    String msgcontent = contentValues.getAsString("msgContent");
                    if (isSend == 0 && type == 1){
                            /*
                            参数3 = encryptTalker=v1_cdf8a7b3da023da5307395352cd08500f7b4150729e300531a9da91d1f296ba7@stranger msgContent=<msg fromusername="zzxm88" encryptusername="v1_cdf8a7b3da023da5307395352cd08500f7b4150729e300531a9da91d1f296ba7@stranger" fromnickname="明" content="我是明" fullpy="ming" shortpy="M" imagestatus="3" scene="30" country="DE" province="Hamburg" city="" sign="" percard="1" sex="1" alias="xm________________" weibo="" weibonickname="" albumflag="0" albumstyle="0" albumbgimgid="912895298764800_912895298764800" snsflag="48" snsbgimgid="" snsbgobjectid="0" mhash="1b3dad158913a69d934caaadde76a00f" mfullhash="1b3dad158913a69d934caaadde76a00f" bigheadimgurl="http://wx.qlogo.cn/mmhead/ver_1/OGlGw9icNc0W5md9kfaC54hRN6zsxxLicIiboXkjYTSUs6FEhvV56QLz5icRLdkbicSsZCktcxiaQKT0qZsSEk6MYwng/0" smallheadimgurl="http://wx.qlogo.cn/mmhead/ver_1/OGlGw9icNc0W5md9kfaC54hRN6zsxxLicIiboXkjYTSUs6FEhvV56QLz5icRLdkbicSsZCktcxiaQKT0qZsSEk6MYwng/96" ticket="v2_c9ded7a183c8bc8c587756da612cea8fadc77558e98239c10d8bb1ff590f5a0088bdd7ef3ae4f6e99acc93e160c2baec@stranger" opcode="2" googlecontact="" qrticket="" chatroomusername="" sourceusername="" sourcenickname=""><brandlist count="0" ver="698050163"></brandlist></msg> chatroomName= svrId=5540230629124514099 createTime=1526281451000 talker=zzxm88 type=1 isSend=0
                            */


                        DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                        Document parse = newDocumentBuilder.parse(new ByteArrayInputStream(msgcontent.getBytes()));

                        String ticket = parse.getFirstChild().getAttributes().getNamedItem("ticket").getNodeValue();

                        Object requestCaller = callStaticMethod(findClass("com.tencent.mm.z.au", lpparam.classLoader), "Dv");
                        Object luckyMoneyRequest = newInstance(findClass("com.tencent.mm.pluginsdk.model.m", lpparam.classLoader),
                                3,talker,ticket,30);
                        callMethod(requestCaller, "a", luckyMoneyRequest, 0);

                    }

                }

            }
        });
    }
}
