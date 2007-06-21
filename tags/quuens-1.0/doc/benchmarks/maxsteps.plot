set terminal pdf fname "enhanced" fname "Helvetica"  fsize "8pt"
set out "maxsteps.pdf"
set autoscale
unset log
unset label
#set yrange [0:750]
set xrange [0:5]
set ytics auto nomirror
unset xtics
set title "NQueens (N=18)"
set xlabel ""
set ylabel "Time (seconds)"
unset yzeroaxis 
unset x2zeroaxis
unset xzeroaxis
unset border
set bmargin 10
set grid noxtics ytics front linetype rgb "#FFFFFF"
set boxwidth 0.2 absolute
plot 'bar-graph-data-maxsteps' using 1:3 title '' with boxes fs solid 0.5 lt 2, \
	   ''								using 1:3:3 with labels center offset 0,1 notitle, \
		 ''							  using 1:4:2 with labels right rotate by 50 offset graph -0.001,-0.03 notitle
