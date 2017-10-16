export class Lib {
    static randomStr(len: number): string {
        let alpha: string = 'abcdefghijklmnopqrstuvwxyz';
        let str: string = '';
        for (let i: number = 0; i < len; i++) {
            str += alpha.charAt(Math.floor(Math.random() * alpha.length));
        }
        return str;
    }

    static now(): number {
        return new Date().getTime();
    }
}