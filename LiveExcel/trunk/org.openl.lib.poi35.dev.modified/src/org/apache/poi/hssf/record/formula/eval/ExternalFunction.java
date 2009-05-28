/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package org.apache.poi.hssf.record.formula.eval;

import org.apache.poi.hssf.record.formula.toolpack.MainToolPacksHandler;
import org.apache.poi.hssf.record.formula.functions.FreeRefFunction;
import org.apache.poi.ss.formula.EvaluationWorkbook;
import org.apache.poi.ss.formula.eval.NotImplementedException;
/**
 * 
 * Common entry point for all user-defined (non-built-in) functions (where 
 * <tt>AbstractFunctionPtg.field_2_fnc_index</tt> == 255)
 * 
 * TODO rename to UserDefinedFunction
 * @author Josh Micich
 */
final class ExternalFunction implements FreeRefFunction {

	public ValueEval evaluate(Eval[] args, EvaluationWorkbook workbook, 
			int srcCellSheet, int srcCellRow,int srcCellCol) {
		
		int nIncomingArgs = args.length;
		if(nIncomingArgs < 1) {
			throw new RuntimeException("function name argument missing");
		}
		
		Eval nameArg = args[0];
		FreeRefFunction targetFunc;
		if (nameArg instanceof NameEval) {
			targetFunc = findInternalUserDefinedFunction((NameEval) nameArg);
		} else if (nameArg instanceof NameXEval) {
			targetFunc = findExternalUserDefinedFunction(workbook, (NameXEval) nameArg);
		} else {
			throw new RuntimeException("First argument should be a NameEval, but got ("
					+ nameArg.getClass().getName() + ")");
		}
		int nOutGoingArgs = nIncomingArgs -1;
		Eval[] outGoingArgs = new Eval[nOutGoingArgs];
		System.arraycopy(args, 1, outGoingArgs, 0, nOutGoingArgs);
		return targetFunc.evaluate(outGoingArgs, workbook, srcCellSheet, srcCellRow, srcCellCol);
	}

	private static FreeRefFunction findExternalUserDefinedFunction(EvaluationWorkbook workbook,
			NameXEval n) {
		String functionName = workbook.resolveNameXText(n.getPtg());

		if(false) {
			System.out.println("received call to external user defined function (" + functionName + ")");
		}
		// currently only looking for functions from the 'Analysis TookPak'  e.g. "YEARFRAC" or "ISEVEN"
		// not sure how much this logic would need to change to support other or multiple add-ins.
		FreeRefFunction result = MainToolPacksHandler.instance().findFunction(functionName);
		if (result != null) {
			return result;
		}
		throw new NotImplementedException(functionName);
	}

	private static FreeRefFunction findInternalUserDefinedFunction(NameEval functionNameEval) {

		String functionName = functionNameEval.getFunctionName();
		if(false) {
			System.out.println("received call to internal user defined function  (" + functionName + ")");
		}
		FreeRefFunction functionEvaluator = MainToolPacksHandler.instance().findFunction(functionName);
        if (functionEvaluator != null) {
            return functionEvaluator;
        }
		// TODO find the implementation for the user defined function
		
		throw new NotImplementedException(functionName);
	}
}
