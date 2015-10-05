getblock 0 -1 0
gettype @block
.ifeq 5 @type wool 
dig 0 -1 0
slot 1
place 0 -1 0
move 0 0 -1
scriptH
.ifeq 2 @type stone 
move 0 0 1
scriptD