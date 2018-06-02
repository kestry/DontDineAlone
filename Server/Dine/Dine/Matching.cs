using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Dine
{
    public class Matching
    {
        private static ConcurrentDictionary<string, int> nine_ten = new ConcurrentDictionary<string, int>();
        private static ConcurrentDictionary<string, int> cowell_stevenson = new ConcurrentDictionary<string, int>();
        private static ConcurrentDictionary<string, int> crown_merrill = new ConcurrentDictionary<string, int>();
        private static ConcurrentDictionary<string, int> porter_kresge = new ConcurrentDictionary<string, int>();
        private static ConcurrentDictionary<string, int> rc_oakes = new ConcurrentDictionary<string, int>();

        public static string tryMatch(MatchQuery matchQuery)
        {
            string partnerId = "";
            string college = "";
            int state = -1;
            foreach(KeyValuePair<string, int> q in matchQuery.getPreferences())
            {
                college = q.Key;
                state = q.Value;
                if (state == 0)
                    continue;
                switch(college)
                {
                    case "nine/ten":
                        {
                            if(nine_ten.Count > 0)
                                partnerId = nine_ten.FirstOrDefault().Key;
                            else
                                nine_ten.GetOrAdd(matchQuery.getUserId(), 1);
                            break;
                        }
                    case "cowell/stevenson":
                        {
                            if (cowell_stevenson.Count > 0)
                                partnerId = cowell_stevenson.FirstOrDefault().Key;
                            else
                                cowell_stevenson.GetOrAdd(matchQuery.getUserId(), 1);
                            break;
                        }
                    case "crown/merill":
                        {
                            if (crown_merrill.Count > 0)
                                partnerId = crown_merrill.FirstOrDefault().Key;
                            else
                                crown_merrill.GetOrAdd(matchQuery.getUserId(), 1);
                            break;
                        }
                    case "porter/kresge":
                        {
                            if (porter_kresge.Count > 0)
                                partnerId = porter_kresge.FirstOrDefault().Key;
                            else
                                porter_kresge.GetOrAdd(matchQuery.getUserId(), 1);
                            break;
                        }
                    case "rc/oakes":
                        {
                            if (rc_oakes.Count > 0)
                                partnerId = rc_oakes.FirstOrDefault().Key;
                            else
                                rc_oakes.GetOrAdd(matchQuery.getUserId(), 1);
                            break;
                        }
                }
                if (!string.IsNullOrEmpty(partnerId))
                    break;
            }
            if (!string.IsNullOrEmpty(partnerId))
            {
                remove(matchQuery.getUserId());
                remove(partnerId);
            }
            Console.WriteLine("======tryMatch=======");
            Console.WriteLine("Id: " + matchQuery.getUserId());
            Console.WriteLine("Nine/Ten - " + nine_ten.Count());
            Console.WriteLine("Cowell/Stevenson - " + cowell_stevenson.Count());
            Console.WriteLine("Crown/Merrill - " + crown_merrill.Count());
            Console.WriteLine("Porter/Kresge - " + porter_kresge.Count());
            Console.WriteLine("RC/Oakes - " + rc_oakes.Count());
            Console.WriteLine("=====================");
            return partnerId;
        }

        public static bool remove(string id)
        {
            int result = -1;
            nine_ten.TryRemove(id, out result);
            cowell_stevenson.TryRemove(id, out result);
            crown_merrill.TryRemove(id, out result);
            porter_kresge.TryRemove(id, out result);
            rc_oakes.TryRemove(id, out result);
            Console.WriteLine("======deQueue=======");
            Console.WriteLine("Id: " + id);
            Console.WriteLine("Nine/Ten - " + nine_ten.Count());
            Console.WriteLine("Cowell/Stevenson - " + cowell_stevenson.Count());
            Console.WriteLine("Crown/Merrill - " + crown_merrill.Count());
            Console.WriteLine("Porter/Kresge - " + porter_kresge.Count());
            Console.WriteLine("RC/Oakes - " + rc_oakes.Count());
            Console.WriteLine("=====================");
            return true;
        }
    }
}
