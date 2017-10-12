var lib: any = {};

lib.randomStr = function (len: number) {
    var alpha = 'abcdefghijklmnopqrstuvwxyz';
    var str = '';
    for (let i: number = 0; i < len; i++) {
        str += alpha.charAt(Math.floor(Math.random() * alpha.length));
    }
    return str;
};

module.exports = {
    lib: lib
};