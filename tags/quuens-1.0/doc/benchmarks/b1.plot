set terminal pdf fname "enhanced" fname "Helvetica"  fsize "8pt"
set out "b1.pdf"
set autoscale
unset log
unset label
set yrange [0:750]
set xtic 1.0
set ytic auto
set title "NQueens (Size 18)"
set xlabel "Test nr"
set ylabel "Time (seconds)"
plot 'benchmark_c_18_2007-06-02-22-20-44' using 4 title 'C kode' with linespoints, \
     'benchmark_java_18_2007-06-02-22-20-44' using 4 title 'Java port' with linespoints, \
     'benchmark_java_18_1_true_2007-06-03-00-13-44' using 4 title 'Java rekursiv (1)' with linespoints, \
     'benchmark_java_18_1_false_2007-06-03-10-55-54' using 4 title 'Java iterativ (1)' with linespoints
