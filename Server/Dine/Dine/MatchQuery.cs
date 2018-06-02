using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Dine
{
    public class MatchQuery
    {
        private string userId = "";
        private Dictionary<string, int> preferences = null;

        public MatchQuery(string userId)
        {
            this.userId = userId;
            this.preferences = new Dictionary<string, int>();
            this.preferences.Add("nine/ten", 0);
            this.preferences.Add("cowell/stevenson", 0);
            this.preferences.Add("crown/merill", 0);
            this.preferences.Add("porter/kresge", 0);
            this.preferences.Add("rc/oakes", 0);
        }

        public string getUserId()
        {
            return userId;
        }
        
        public void update(string name)
        {
            if(preferences.ContainsKey(name))
            {
                preferences[name] = 1;
            }
        }

        public Dictionary<string, int> getPreferences()
        {
            return preferences;
        }
    }
}
