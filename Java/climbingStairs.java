class Solution {
    //iterative dp
    public int climbStairs(int n) {
     int[] dp = new int[n+1];
        dp[0]=1;
        
        for(int i=1;i<=n;i++){
            if(i==1){
                dp[i]=dp[i-1];
            }
            else if(i==2){
                dp[i]=dp[i-1] + dp[i-2];    
            }
            else{
                dp[i] = dp[i-1] + dp[i-2];
            }
        }
        return dp[n];     
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