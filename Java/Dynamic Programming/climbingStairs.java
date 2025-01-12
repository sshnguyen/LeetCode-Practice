class Solution {
    //iterative dp
    class Solution {
    public int climbStairs(int n) {
        // distinctWays[i] store the number of distinct ways to reach i steps.
        int[] distinctWays = new int[n + 1];
        //base cases
        distinctWays[0] = 1;
        distinctWays[1] = 1;
        
        for (int i = 2; i <= n; i++){
            distinctWays[i] = distinctWays[i - 2] + distinctWays[i - 1];
        }
        
        return distinctWays[n];
    }
}
    //Memoization
    public int climbStairs(int n) {
        return countPaths(n,new int[n+1]);
    }
    public static int countPaths(int n,int[] dp) {
		if(n==0) {
			return 1;
		}
		if(n<0) {
			return 0;
		}
		if(dp[n]>0) {
			return dp[n];
		}
		
		int nm1=countPaths(n-1,dp);
		int nm2=countPaths(n-2,dp);
		int cp = nm1 + nm2;
		
		dp[n]=cp;
		return cp;
	}
}