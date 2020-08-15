declare const window: any;
declare const wx: any;
export default class Utils {
    public static setSessionItem(key: string, obj: any) {
        if (obj == null) {
            window.sessionStorage.removeItem(key);
            return;
        }
        if (typeof obj === 'string' || typeof obj === 'number') {
            return window.sessionStorage.setItem(key, obj);
        }
        window.sessionStorage.setItem(key, JSON.stringify(obj));
    }

    public static setItem(key: string, obj: any) {
        if (obj == null) {
            window.localStorage.removeItem(key);
            return;
        }
        if (typeof obj === 'string' || typeof obj === 'number') {
            return window.localStorage.setItem(key, obj);
        }
        window.localStorage.setItem(key, JSON.stringify(obj));
    }

    public static clear() {
        window.localStorage.clear();
        window.sessionStorage.clear();
    }

    public static getSessionItem(key: string) {
        const json = window.sessionStorage.getItem(key);
        if (json != null && (json[0] === '{' || json[0] === '[')) {
            return JSON.parse(json);
        }
        return json;
    }

    public static getItem(key: string) {
        const json = window.localStorage.getItem(key);
        if (json != null && (json[0] === '{' || json[0] === '[')) {
            return JSON.parse(json);
        }
        return json;
    }

    public static async toPage(that: any, url: string, args: any) {
        if (url === '') {
            switch (await this.getDeviceType()) {
                case 'H5':
                case 'WE_CHAT_H5':
                    that.$router.go(-1);
                    break;
                case 'WE_CHAT':
                    wx.miniProgram.navigateBack({
                        delta: 1,
                    });
            }
            return;
        }
        url = url + '?';
        if (args != null) {
            for (const name in args) {
                if (args.hasOwnProperty(name)) {
                    url = url + name + '=' + args[name] + '&';
                }
            }
        }

        // 默认跳转
        switch (await this.getDeviceType()) {
            case 'H5':
            case 'WE_CHAT_H5':
                await that.$router.push(url);
                break;
            case 'WE_CHAT':
                wx.miniProgram.navigateTo({
                    url: '/pages/shop/webview/webview?url=' + encodeURIComponent(url),
                });
                break;
        }
    }

    public static async reset() {
        switch (await this.getDeviceType()) {
            case 'APP':
            case 'H5':
            case 'WE_CHAT_H5':
                // window.location.href = window.location.origin + '/user/login';
                break;
            case 'WE_CHAT':
                wx.miniProgram.redirectTo('/pages/shop/shop');
                break;
        }
    }

    public static async toShopHome() {
        switch (await this.getDeviceType()) {
            case 'H5':
            case 'WE_CHAT_H5':
                window.location.href = window.location.origin + '/shop/home/0';
                break;
            case 'WE_CHAT':
                wx.miniProgram.redirectTo('/pages/shop/webview/webview');
                break;
        }
    }


    public static getParam(key: string) {
        const value = window.location.href.split('?')[1];
        if (value == null) {
            return null;
        }
        let m = '';
        decodeURIComponent(value)
            .split('&')
            .forEach(it => {
                const ss = it.trim().split('=');
                if (ss[0].toString().toLocaleLowerCase() === key.toLocaleLowerCase()) {
                    m = ss[1];
                }
            });
        return m;
    }

    public static async getDeviceType(): Promise<string> {
        const agent = window.navigator.userAgent.toLowerCase();
        if (
            wx != null &&
            wx.miniProgram != null &&
            (agent.indexOf('micromessenger') > 0 || agent.indexOf('wechatdevtools') > 0)
        ) {
            return await new Promise<string>(resolve => {
                wx.miniProgram.getEnv(res => {
                    if (!res.miniprogram) {
                        resolve('WE_CHAT_H5');
                    } else {
                        resolve('WE_CHAT');
                    }
                });
            });
        } else {
            return 'H5';
        }
    }


    static async getUrlPath() {
        const s = window.location.href.toString().split('?');
        return s[0].toLowerCase().replace('http://', '').replace("https://", '').split('/');
    }


    static dateFormat(fmt, date) {
        let ret;
        const opt = {
            "yyyy": date.getFullYear().toString(),        // 年
            "MM": (date.getMonth() + 1).toString(),     // 月
            "dd": date.getDate().toString(),            // 日
            "HH": date.getHours().toString(),           // 时
            "mm": date.getMinutes().toString(),         // 分
            "ss": date.getSeconds().toString()          // 秒
        };
        for (let k in opt) {
            ret = new RegExp("(" + k + ")").exec(fmt);
            if (ret) {
                fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")));
            }
        }
        return fmt;
    }

    static setTitle(title: string) {
        window.document.title = title + '_' + window.document.title
    }
}
