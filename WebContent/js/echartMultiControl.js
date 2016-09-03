/**
 * 
 */

var graph;
var femaleShow = true;
var maleShow = true;
var defaultColor = [ '#c23531', '#2f4554', '#61a0a8', '#d48265', '#91c7ae',
	'#749f83', '#ca8622', '#bda29a', '#6e7074', '#546570', '#c4ccd3' ];
var grey = '#777777'

option = {
	title : {
		text : '男性女性身高体重分布',
		subtext : '抽样调查来自: Heinz  2003'
	},
	grid : {
		left : '3%',
		right : '7%',
		bottom : '3%',
		containLabel : true
	},
	tooltip : {
		trigger : 'axis',
		showDelay : 0,
		formatter : function(params) {
			if (params.value.length > 1) {
				return params.seriesName + ' :<br/>' + params.value[0] + 'cm '
					+ params.value[1] + 'kg ';
			} else {
				return params.seriesName + ' :<br/>' + params.name + ' : '
					+ params.value + 'kg ';
			}
		},
		axisPointer : {
			show : true,
			type : 'cross',
			lineStyle : {
				type : 'dashed',
				width : 1
			}
		}
	},
	toolbox : {
		feature : {
			dataZoom : {},
			brush : {
				type : [ 'rect', 'polygon', 'clear' ]
			}
		}
	},
	brush : {},
	legend : {
		data : [ '女性', '男性' ],
		left : 'center'
	},
	xAxis : [ {
		type : 'value',
		scale : true,
		axisLabel : {
			formatter : '{value} cm'
		},
		splitLine : {
			show : false
		}
	} ],
	yAxis : [ {
		type : 'value',
		scale : true,
		axisLabel : {
			formatter : '{value} kg'
		},
		splitLine : {
			show : false
		}
	} ],
	series : [ {
		name : '女性',
		type : 'scatter',
		data : femaledata,
		markArea : {
			silent : true,
			itemStyle : {
				normal : {
					color : 'transparent',
					borderWidth : 1,
					borderType : 'dashed'
				}
			},
			data : [ [ {
				name : '女性分布区间',
				xAxis : 'min',
				yAxis : 'min'
			}, {
				xAxis : 'max',
				yAxis : 'max'
			} ] ]
		},
		markPoint : {
			data : [ {
				type : 'max',
				name : '最大值'
			}, {
				type : 'min',
				name : '最小值'
			} ]
		},
		markLine : {
			lineStyle : {
				normal : {
					type : 'solid'
				}
			},
			data : [ {
				type : 'average',
				name : '平均值'
			}, {
				xAxis : 160
			} ]
		}
	}, {
		name : '男性',
		type : 'scatter',
		data : maledata,
		markArea : {
			silent : true,
			itemStyle : {
				normal : {
					color : 'transparent',
					borderWidth : 1,
					borderType : 'dashed'
				}
			},
			data : [ [ {
				name : '男性分布区间',
				xAxis : 'min',
				yAxis : 'min'
			}, {
				xAxis : 'max',
				yAxis : 'max'
			} ] ]
		},
		markPoint : {
			data : [ {
				type : 'max',
				name : '最大值'
			}, {
				type : 'min',
				name : '最小值'
			} ]
		},
		markLine : {
			lineStyle : {
				normal : {
					type : 'solid'
				}
			},
			data : [ {
				type : 'average',
				name : '平均值'
			}, {
				xAxis : 170
			} ]
		}
	} ]
};

function renderChart() {
	var colors = defaultColor.slice(0);
	if (!femaleShow) {
		colors[0] = grey;
	}
	if (!maleShow) {
		colors[1] = grey;
	}
	option.color = colors;
	graph.setOption(option);

}

function renderButton() {
	var buttonRoot = document.getElementById('buttonroot');

	var femaleButton = document.createElement('button');
	femaleButton.onclick = function() {
		femaleShow = true;
		maleShow = false;
		renderChart();
	}
	femaleButton.innerText = 'female';
	var maleButton = document.createElement('button');
	maleButton.onclick = function() {
		femaleShow = false;
		maleShow = true;
		renderChart();
	}
	maleButton.innerText = 'male';
	var bothButton = document.createElement('button');
	bothButton.onclick = function() {
		femaleShow = true;
		maleShow = true;
		renderChart();
	}
	bothButton.innerText = 'both';
	buttonRoot.appendChild(femaleButton);
	buttonRoot.appendChild(maleButton);
	buttonRoot.appendChild(bothButton);
}

function onload() {
	graph = echarts.init(document.getElementById('chart'));
	renderButton();
	renderChart();
}