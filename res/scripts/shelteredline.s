sudo /clear
look 0 0
slot 0
sudo /i stone 64
slot 1
sudo /i redstone 64
.for 14 10
  dig 0 0 1
  dig 0 1 1
  dig 1 0 1
  dig 1 1 1
  dig -1 0 1
  dig -1 1 1
  slot 0
  place 1 0 1
  place 1 1 1
  place -1 0 1
  place -1 1 1
  slot 1
  place 0 0 1
  move 0 0 1