package sql.jsqlparser;

import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnalyticExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.CastExpression;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.ExtractExpression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.HexValue;
import net.sf.jsqlparser.expression.IntervalExpression;
import net.sf.jsqlparser.expression.JdbcNamedParameter;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.JsonExpression;
import net.sf.jsqlparser.expression.KeepExpression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.MySQLGroupConcat;
import net.sf.jsqlparser.expression.NotExpression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.NumericBind;
import net.sf.jsqlparser.expression.OracleHierarchicalExpression;
import net.sf.jsqlparser.expression.OracleHint;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.RowConstructor;
import net.sf.jsqlparser.expression.SignedExpression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeKeyExpression;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.UserVariable;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.WithinGroupExpression;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Modulo;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.JsonOperator;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperator;
import net.sf.jsqlparser.expression.operators.relational.RegExpMySQLOperator;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.SetStatement;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.create.index.CreateIndex;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.view.AlterView;
import net.sf.jsqlparser.statement.create.view.CreateView;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.execute.Execute;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.merge.Merge;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.FromItemVisitor;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.TableFunction;
import net.sf.jsqlparser.statement.select.ValuesList;
import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;

public final class SQLColumnAndTableFinder implements SelectVisitor, FromItemVisitor, ExpressionVisitor,
		ItemsListVisitor, SelectItemVisitor, StatementVisitor {

	/**
	 * @description 不支持方法的异常消息
	 */
	private static final String NOT_SUPPORTED_YET = "Not supported yet.";

	/**
	 * @description Select表达式对象 样例: Select col1,CONCAT(colmn2,colmn3) From table
	 *              中的col1与CONCAT(colmn2,colmn3)
	 */
	private List<SelectExpressionItem> selectExpressionItems;

	/**
	 * @description TQL中的Column
	 * @value value:columns
	 */
	private List<Column> columns;

	/**
	 * @description TQL中的Table
	 */
	private List<Table> tables;

	/**
	 * @name 获取选择表达式
	 */
	public List<SelectExpressionItem> getSelectExpressionItems() {
		return selectExpressionItems;
	}

	public List<Column> getColumns() {
		return columns;
	}

	/**
	 * @name 获取所有表
	 */
	public List<Table> getTables() {
		return tables;
	}

	public void attach(Select select) {
		init();
		select.accept(this);
	}

	@Override
	public void visit(Select select) {
		if (select.getWithItemsList() != null) {
			for (WithItem withItem : select.getWithItemsList()) {
				withItem.accept(this);
			}
		}
		select.getSelectBody().accept(this);
	}

	public void visit(WithItem withItem) {
		withItem.getSelectBody().accept(this);
	}

	@Override
	public void visit(PlainSelect plainSelect) {
		// select
		if (plainSelect.getSelectItems() != null) {
			for (SelectItem item : plainSelect.getSelectItems()) {
				item.accept(this);
			}
		}
		// From
		if (plainSelect.getFromItem() != null) {
			plainSelect.getFromItem().accept(this);
		}
		// join
		if (plainSelect.getJoins() != null) {
			for (Join join : plainSelect.getJoins()) {
				join.getRightItem().accept(this);
				if (join.getOnExpression() != null) {
					join.getOnExpression().accept(this);
				}
			}
		}
		// where
		if (plainSelect.getWhere() != null) {
			plainSelect.getWhere().accept(this);
		}
		if (plainSelect.getOracleHierarchical() != null) {
			plainSelect.getOracleHierarchical().accept(this);
		}
	}

	@Override
	public void visit(Table table) {
		tables.add(table);
	}

	@Override
	public void visit(SubSelect subSelect) {
		if (subSelect.getWithItemsList() != null) {
			for (WithItem withItem : subSelect.getWithItemsList()) {
				withItem.accept(this);
			}
		}
		subSelect.getSelectBody().accept(this);
	}

	@Override
	public void visit(Addition addition) {
		visitBinaryExpression(addition);
	}

	@Override
	public void visit(AndExpression andExpression) {
		visitBinaryExpression(andExpression);
	}

	@Override
	public void visit(Between between) {
		between.getLeftExpression().accept(this);
		between.getBetweenExpressionStart().accept(this);
		between.getBetweenExpressionEnd().accept(this);
	}

	@Override
	public void visit(Column tableColumn) {
		columns.add(tableColumn);
	}

	@Override
	public void visit(Division division) {
		visitBinaryExpression(division);
	}

	@Override
	public void visit(DoubleValue doubleValue) {
	}

	@Override
	public void visit(EqualsTo equalsTo) {
		visitBinaryExpression(equalsTo);
	}

	@Override
	public void visit(Function function) {
		if (function.getParameters() != null) {
			visit(function.getParameters());
		}
	}

	@Override
	public void visit(GreaterThan greaterThan) {
		visitBinaryExpression(greaterThan);
	}

	@Override
	public void visit(GreaterThanEquals greaterThanEquals) {
		visitBinaryExpression(greaterThanEquals);
	}

	@Override
	public void visit(InExpression inExpression) {
		if (inExpression.getLeftExpression() != null) {
			inExpression.getLeftExpression().accept(this);
		} else if (inExpression.getLeftItemsList() != null) {
			inExpression.getLeftItemsList().accept(this);
		}
		inExpression.getRightItemsList().accept(this);
	}

	@Override
	public void visit(SignedExpression signedExpression) {
		signedExpression.getExpression().accept(this);
	}

	@Override
	public void visit(IsNullExpression isNullExpression) {
	}

	@Override
	public void visit(JdbcParameter jdbcParameter) {
	}

	@Override
	public void visit(LikeExpression likeExpression) {
		visitBinaryExpression(likeExpression);
	}

	@Override
	public void visit(ExistsExpression existsExpression) {
		existsExpression.getRightExpression().accept(this);
	}

	@Override
	public void visit(LongValue longValue) {
	}

	@Override
	public void visit(MinorThan minorThan) {
		visitBinaryExpression(minorThan);
	}

	@Override
	public void visit(MinorThanEquals minorThanEquals) {
		visitBinaryExpression(minorThanEquals);
	}

	@Override
	public void visit(Multiplication multiplication) {
		visitBinaryExpression(multiplication);
	}

	@Override
	public void visit(NotEqualsTo notEqualsTo) {
		visitBinaryExpression(notEqualsTo);
	}

	@Override
	public void visit(NullValue nullValue) {
	}

	@Override
	public void visit(OrExpression orExpression) {
		visitBinaryExpression(orExpression);
	}

	@Override
	public void visit(Parenthesis parenthesis) {
		parenthesis.getExpression().accept(this);
	}

	@Override
	public void visit(StringValue stringValue) {
	}

	@Override
	public void visit(Subtraction subtraction) {
		visitBinaryExpression(subtraction);
	}

	@Override
	public void visit(NotExpression notExpr) {
		notExpr.getExpression().accept(this);
	}

	public void visitBinaryExpression(BinaryExpression binaryExpression) {
		binaryExpression.getLeftExpression().accept(this);
		binaryExpression.getRightExpression().accept(this);
	}

	@Override
	public void visit(ExpressionList expressionList) {
		for (Expression expression : expressionList.getExpressions()) {
			expression.accept(this);
		}
	}

	@Override
	public void visit(DateValue dateValue) {
	}

	@Override
	public void visit(TimestampValue timestampValue) {
	}

	@Override
	public void visit(TimeValue timeValue) {
	}

	@Override
	public void visit(CaseExpression caseExpression) {
	}

	@Override
	public void visit(WhenClause whenClause) {
	}

	@Override
	public void visit(AllComparisonExpression allComparisonExpression) {
		allComparisonExpression.getSubSelect().getSelectBody().accept(this);
	}

	@Override
	public void visit(AnyComparisonExpression anyComparisonExpression) {
		anyComparisonExpression.getSubSelect().getSelectBody().accept(this);
	}

	@Override
	public void visit(SubJoin subjoin) {
		subjoin.getLeft().accept(this);
		subjoin.getJoin().getRightItem().accept(this);
	}

	@Override
	public void visit(Concat concat) {
		visitBinaryExpression(concat);
	}

	@Override
	public void visit(Matches matches) {
		visitBinaryExpression(matches);
	}

	@Override
	public void visit(BitwiseAnd bitwiseAnd) {
		visitBinaryExpression(bitwiseAnd);
	}

	@Override
	public void visit(BitwiseOr bitwiseOr) {
		visitBinaryExpression(bitwiseOr);
	}

	@Override
	public void visit(BitwiseXor bitwiseXor) {
		visitBinaryExpression(bitwiseXor);
	}

	@Override
	public void visit(CastExpression cast) {
		cast.getLeftExpression().accept(this);
	}

	@Override
	public void visit(Modulo modulo) {
		visitBinaryExpression(modulo);
	}

	@Override
	public void visit(AnalyticExpression analytic) {
	}

	@Override
	public void visit(SetOperationList list) {
		for (SelectBody plainSelect : list.getSelects()) {
			plainSelect.accept(this);
		}
	}

	@Override
	public void visit(ExtractExpression eexpr) {
	}

	@Override
	public void visit(LateralSubSelect lateralSubSelect) {
		lateralSubSelect.getSubSelect().getSelectBody().accept(this);
	}

	@Override
	public void visit(MultiExpressionList multiExprList) {
		for (ExpressionList exprList : multiExprList.getExprList()) {
			exprList.accept(this);
		}
	}

	@Override
	public void visit(ValuesList valuesList) {
	}

	protected void init() {
		selectExpressionItems = new ArrayList<SelectExpressionItem>();
		columns = new ArrayList<Column>();
		tables = new ArrayList<Table>();
	}

	@Override
	public void visit(IntervalExpression iexpr) {
	}

	@Override
	public void visit(JdbcNamedParameter jdbcNamedParameter) {
	}

	@Override
	public void visit(OracleHierarchicalExpression oexpr) {
		if (oexpr.getStartExpression() != null) {
			oexpr.getStartExpression().accept(this);
		}

		if (oexpr.getConnectExpression() != null) {
			oexpr.getConnectExpression().accept(this);
		}
	}

	@Override
	public void visit(RegExpMatchOperator rexpr) {
		visitBinaryExpression(rexpr);
	}

	@Override
	public void visit(RegExpMySQLOperator rexpr) {
		visitBinaryExpression(rexpr);
	}

	@Override
	public void visit(JsonExpression jsonExpr) {
	}

	@Override
	public void visit(JsonOperator jsonExpr) {
	}

	@Override
	public void visit(AllColumns allColumns) {
	}

	@Override
	public void visit(AllTableColumns allTableColumns) {
	}

	@Override
	public void visit(SelectExpressionItem item) {
		this.selectExpressionItems.add(item);
		item.getExpression().accept(this);
	}

	@Override
	public void visit(WithinGroupExpression wgexpr) {
	}

	@Override
	public void visit(UserVariable var) {
	}

	@Override
	public void visit(NumericBind bind) {

	}

	@Override
	public void visit(KeepExpression aexpr) {
	}

	@Override
	public void visit(MySQLGroupConcat groupConcat) {
	}

	@Override
	public void visit(Delete delete) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
	}

	@Override
	public void visit(Update update) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
	}

	@Override
	public void visit(Insert insert) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
	}

	@Override
	public void visit(Replace replace) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
	}

	@Override
	public void visit(Drop drop) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
	}

	@Override
	public void visit(Truncate truncate) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
	}

	@Override
	public void visit(CreateIndex createIndex) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
	}

	@Override
	public void visit(CreateTable create) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
	}

	@Override
	public void visit(CreateView createView) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
	}

	@Override
	public void visit(Alter alter) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
	}

	@Override
	public void visit(Statements stmts) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
	}

	@Override
	public void visit(Execute execute) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
	}

	@Override
	public void visit(SetStatement set) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
	}

	@Override
	public void visit(RowConstructor rowConstructor) {
		for (Expression expr : rowConstructor.getExprList().getExpressions()) {
			expr.accept(this);
		}
	}

	@Override
	public void visit(HexValue hexValue) {

	}

	@Override
	public void visit(Merge merge) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
	}

	@Override
	public void visit(OracleHint hint) {
	}

	@Override
	public void visit(TableFunction valuesList) {
	}

	@Override
	public void visit(AlterView alterView) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
	}

	@Override
	public void visit(TimeKeyExpression timeKeyExpression) {
	}

	@Override
	public void visit(DateTimeLiteralExpression literal) {

	}
}
