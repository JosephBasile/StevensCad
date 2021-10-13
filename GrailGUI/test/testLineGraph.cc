#include "opengl/GrailGUI.hh"
#include "opengl/LineGraphWidget.hh"

using namespace std;
using namespace grail;

class TestLineGraph : public GLWin {
 private:
  Style *baseGraphStyle;
  Style *xAxisStyle;
  Style *xAxisTextStyle;
  Style *yAxisStyle;
  Style *yAxisTextStyle;
  Style *dataStyle;

 public:
  TestLineGraph() : GLWin(0x000000, 0xCCCCCC, "TestLineGraph") {}
  ~TestLineGraph() {
    delete baseGraphStyle;
    delete xAxisStyle;
    delete xAxisTextStyle;
    delete yAxisStyle;
    delete yAxisTextStyle;
    delete dataStyle;
  }

  void init() {
    // two lines and the overall title
    baseGraphStyle = new Style("TIMES", 12, 1, 0, 0, 0, 0, 0, 0);
    baseGraphStyle->setLineWidth(5);

    // will control how thick lines for x axis are drawn
    xAxisStyle = new Style("TIMES", 12, 1, 0, 0, 0, 0, 1, 0);
    xAxisStyle->setLineWidth(4);

    // controls the font, size, and color of x axis text
    xAxisTextStyle = new Style("TIMES", 12, 1, 0, 0, 0, 1, 0, 0);
    xAxisTextStyle->setLineWidth(3);

    // will control how thick lines for y axis are drawn
    yAxisStyle = new Style("TIMES", 12, 1, 0, 0, 0, 0, 0, 1);
    yAxisStyle->setLineWidth(2);

    // controls the font, size, and color of y axis text
    yAxisTextStyle = new Style("TIMES", 12, 1, 0, 0, 0, 1, 0, 1);
    yAxisTextStyle->setLineWidth(1);

    // controls the thickness of lines drawn by LineGraphWidget
    dataStyle = new Style("TIMES", 12, 1, 0, 0, 0, 1, 0, 1);
    dataStyle->setLineWidth(2);

    MainCanvas *c = currentTab()->getMainCanvas();

    vector<double> times = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    vector<double> values = {0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20};
    vector<double> logValues = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024};

    LineGraphWidget lgw(c, 200, 200, 500, 300);

    // Graph Superclass settings
    lgw.setGraphTitle("Test Title");
    lgw.setBaseStyle(baseGraphStyle);
    lgw.setXAxisStyle(xAxisStyle);
    lgw.setYAxisStyle(yAxisStyle);
    lgw.setXAxisTextStyle(xAxisTextStyle);
    lgw.setYAxisTextStyle(yAxisTextStyle);

    // LineGraphWidget specific settings
    lgw.setDataStyle(dataStyle);
    lgw.setPointFormat('t', 5, grail::red);

    // x axis stuff
    lgw.setXPoints(times);
    lgw.createXAxis(GraphWidget::AxisType::LINEAR);

    // set x axis parameters
    lgw.xAxis->setBounds(times.front(), times.back());
    lgw.xAxis->setTickInterval(2);

    lgw.xAxis->setTickDrawSize(10);
    lgw.xAxis->setShowTicks(true);
    lgw.xAxis->setTitle("x axis");
    lgw.xAxis->setAxisColor(grail::green);
    lgw.xAxis->setTickColor(grail::purple);
    lgw.xAxis->setTickFormat(3, 1);

    // y axis stuff
    // lgw.createYAxis(GraphWidget::AxisType::LINEAR);
    // lgw.setYPoints(values);
    lgw.createYAxis(GraphWidget::AxisType::LOGARITHMIC);
    lgw.setYPoints(logValues);

    // set y axis parameters

    // lgw.yAxis->setBounds(values.front(), values.back());
    lgw.yAxis->setBounds(logValues.front(), logValues.back());
    lgw.yAxis->setIsVert(true);
    // linear
    // lgw.yAxis->setTickInterval(3);
    // logarithmic
    lgw.yAxis->setTickInterval(2);
    lgw.yAxis->setTickFormat(4, 1);

    lgw.yAxis->setTickDrawSize(8);
    lgw.yAxis->setShowTicks(true);
    lgw.yAxis->setTitle("y axis");
    lgw.yAxis->setAxisColor(grail::yellow);
    lgw.yAxis->setTickColor(grail::red);

    lgw.init();
  }
};

int main(int argc, char *argv[]) {
  return GLWin::init(new TestLineGraph(), 1000, 1000);
}
