<h1 style="text-align:center">
    CFG化简
</h1>


## 使用方法

在命令行输入`java -jar CFG化简.jar fileA fileB`以使用本机JRE（需要16及以上）

或输入`start jre\bin\java -jar CFG化简.jar fileA fileB`以使用捆绑JRE

- fileA 为待转换的文件名
- fileB 为输出文件名

## 开发环境

JDK SE16

## 文件编写

示例文件CFG.txt由以下4部分组成，输出文件阅读方式与此相同

```
N:
S,A,B,C,D,E
T:
a,b,d,ε
S:
S
P:
S
A,B
A
C,D
B
D,E
C
S,b,ε
D
S,a
E
S,d,ε

```

- N为非终结符集，每个非终结符为一个字符，以`,`隔开
- T为终结符集，每个终结符为一个字符，以`,`隔开，生成式含空，需要将`ε`写入此处
- S为起始符，只为一个非终结符，且在T中出现过
- P为生成式集合，由偶数行组成，两两一对，第一行为生成式左侧，为一个非终结符，第二行为生成式右侧，同左侧的生成式需合并，且以`,`隔开

> 以上提到字符与符号需要用英文字符，使用中文字符表示集合或许能够运行
>
> `,`为英文逗号，不可更改，`ε`为希腊字母epsilon
>
> 在运行算法3生成无空CFG时，用大写字母`Z`表示S<sub>1</sub>，因此`Z`为保留字符，请不要使用，会造成算法3出错
>
> 请保证CFG中至少能生成一个句子（可以为空句子），否则会发生未知错误

## 文件组成

- Main.java 主类，程序入口
- CFG.java 实现化简CFG相关算法并存储CFG



