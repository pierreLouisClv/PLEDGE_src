package pledge.core.techniques.generation.NoveltyScore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.management.JMException;

import org.sat4j.minisat.constraints.cnf.Clauses;

import pledge.core.ModelPLEDGE;

public class ProbSATSolver {

	Random random = new Random();

    int MAX_FLIPS = 4000;

	List<Clause> clauses;

	Map<Integer,List<Clause>> positiveVars;
	Map<Integer,List<Clause>> negativeVars;

    public ProbSATSolver(ModelPLEDGE fm)
    {
        clauses = LoadClauses(fm.getFeatureModelConstraints(), fm.getNamesToFeaturesInt());
		LoadClauseListWhereVarAppears();
    }

	public Binary execute(Binary config) throws JMException 
	{
		InitSatLitteralsCounter(config);
		List<Clause> unsatClauses = FindUnsatClausesList(clauses);
		if (unsatClauses.size() == 0)
		{
			return config;
		}
			
		int flipvar;
		int step = 0;  
		while (unsatClauses.size() != 0
			&& step < MAX_FLIPS)
		{
			Clause randomClause = unsatClauses.size() == 1 ? unsatClauses.get(0) : unsatClauses.get(random.nextInt(0, unsatClauses.size()));
			flipvar = PickVarToFlip(randomClause, config);
			boolean sign = FlipVar(config, flipvar);	
			unsatClauses.removeAll(UpdateClausesSatLitteralsCount(flipvar, true, sign));
			unsatClauses.addAll(UpdateClausesSatLitteralsCount(flipvar, false, !sign));
		}
			
		return config;
	}

	private void LoadClauseListWhereVarAppears()
	{
		positiveVars = new HashMap<Integer,List<Clause>>();
		negativeVars = new HashMap<Integer,List<Clause>>();
		for (Clause clause : clauses)
		{
			for (int lit : clause.GetLitterals())
			{
				int var = Math.abs(lit) - 1;
				if (lit > 0)
				{
					if (!positiveVars.containsKey(var))
					{
						positiveVars.put(var, new ArrayList<Clause>());
					}
					positiveVars.get(var).add(clause);
				}
				else
				{
					if (!negativeVars.containsKey(var))
					{
						negativeVars.put(var, new ArrayList<Clause>());
					}
					negativeVars.get(var).add(clause);
				}
			}
		}
	}
	
	private List<Clause> UpdateClausesSatLitteralsCount(int varFliped, boolean sat, boolean positive)
	{
		List<Clause> clausesToUpdate;
		List<Clause> returnedClauses = new ArrayList<>();
		if (positive)
		{
			clausesToUpdate = positiveVars.get(varFliped);
		}
		else
		{
			clausesToUpdate = negativeVars.get(varFliped);
		}
		if (clausesToUpdate == null) return new ArrayList<>();
		for (Clause clause : clausesToUpdate)
		{
			clause.UpdateSatLitteralsCount(sat);
			if (sat && clause.GetSatLitteralsCount() == 1
				|| !sat && clause.GetSatLitteralsCount() == 0)
			{
				returnedClauses.add(clause);
			}
		}
		return returnedClauses;
	}

	private List<Clause> FindUnsatClausesList(List<Clause> clauses)
	{
		List<Clause> unsatClauses = new ArrayList<>();
		for (Clause clause : clauses)
		{
			if (clause.IsUnsatClause())
			{
				unsatClauses.add(clause);
			}
		}
		return unsatClauses;
	}

	private void InitSatLitteralsCounter(Binary config)
	{
		for (Clause clause : clauses) {
			clause.ResetSatLitCount();
			for (int lit : clause.GetLitterals())
			{
				int var = Math.abs(lit) - 1;
				boolean sign = lit > 0;
				if (config.getIth(var) == sign)
				{
					clause.UpdateSatLitteralsCount(true);
				}
			}
		}
	}

	private int PickVarToFlip(Clause clause, Binary currentConfig)
	{
		List<Clause> impactedClauses;
		double eps = 1.0; 
		double cb = 2.165;

		int [] breakValues = new int [clause.GetLitterals().size()]; // ��¼C��ÿ��������breakֵ
		double [] f = new double [clause.GetLitterals().size()]; // ����ÿ��������fֵ
		double sumF = 0.0;

		int numLits = clause.GetLitterals().size();

		// Calcul des breaks values et fonction pour chaque variable de la clause
		for(int i = 0; i < numLits; i++)
		{
			int lit = clause.GetLitterals().get(i);
			// Variable
			int var = Math.abs(lit) - 1; 
			// On recupère la liste des clauses où la var apparait positivement ou négativement
			if (currentConfig.getIth(var) == true) {
				impactedClauses = positiveVars.get(var);
			} else {
				impactedClauses = negativeVars.get(var);
			}		
			// break value
			breakValues[i] = (impactedClauses != null) ? ProvideBreakValue(impactedClauses) : 0;
			f[i] = Math.pow(eps + breakValues[i], -cb);
			sumF = sumF + f[i];
		}
		// select var based on proba
		double [] accumuProb = SetAccumProba(f, sumF);
		double rnd = random.nextDouble();
		for(int k = 0; k < accumuProb.length; ++k)	{
			if (rnd <= accumuProb[k]) {
				int selectedLit = clause.GetLitterals().get(k);
				return Math.abs(selectedLit) - 1;
			}
		}
		// error
		return -1;	
	}

	private int ProvideBreakValue(List<Clause> impactedClauses)
	{
		int breakVal = 0;
		// Pour chaque clause où la var apparait
		for (Clause impactedClause : impactedClauses) {
			if (impactedClause.GetSatLitteralsCount() == 1)
			{
				++breakVal;
			}
		}
		return breakVal;
	}

	private double[] SetAccumProba(double[] fValues, double sumFValues)
	{
		double [] accumuProb = new double [fValues.length];
		accumuProb[0] = fValues[0] / sumFValues;
		for(int k = 1; k < accumuProb.length; ++k)	{
			accumuProb[k] = accumuProb[k-1] + fValues[k] / sumFValues; // �����ۻ�����
		}
		return accumuProb;
	}

	private boolean FlipVar(Binary config, int varToFlip)
	{
		boolean newBoolVal = !config.getIth(varToFlip);
		config.setIth(varToFlip, newBoolVal);
		return newBoolVal;
	}

    // Conversion by ChatGPT
    private List<Clause> LoadClauses(List<String> stringConstraints, Map<String, Integer> namesToFeaturesInt)
    {
        List<Clause> convertedFeatureModelClauses = new ArrayList<>();
		String[] negationTerms = { "NOT ", "! " }; 
        for (String constraint : stringConstraints) {
			Clause clause = new Clause();
            String[] literals = constraint.split(" OR ");
            for (String literal : literals) {
                literal = literal.trim();
				int positive = 1;
				int removedChars = 0;
				for (String term : negationTerms)
				{
					if (literal.startsWith(term))
					{
						removedChars = term.length();
						positive = -1;
					}
				}
				int featureIndex = positive * namesToFeaturesInt.get(literal.substring(removedChars));
                clause.litterals.add(featureIndex);
            }
            convertedFeatureModelClauses.add(clause);
        }
        return convertedFeatureModelClauses;
    }

	public class Clause {
		private List<Integer> litterals;

		private int satLitteralsCount;

		public Clause()
		{
			litterals = new ArrayList<>();
		}

		public void ResetSatLitCount()
		{
			satLitteralsCount = 0;
		}

		public void UpdateSatLitteralsCount(boolean sat)
		{
			int operation = 1;
			if (!sat) operation *= -1;
			satLitteralsCount = satLitteralsCount + operation;
		}

		public int GetSatLitteralsCount()
		{
			return satLitteralsCount;
		}

		public List<Integer> GetLitterals()
		{
			return litterals;
		}

		public boolean IsUnsatClause()
		{
			return satLitteralsCount == 0;
		}
	}
}
